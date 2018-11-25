import os
import json
import shutil
import numpy as np

from rest_framework.views import APIView
from rest_framework.exceptions import ParseError
from rest_framework.response import Response
from rest_framework import viewsets

from catssports.models import Video, Mistake
from catssports.serializers import VideoSerializer


class PublishVideoView(APIView):

    def put(self, request, format=None):
        if 'file' not in request.data:
            raise ParseError("Empty content " + str(request.data))

        f = request.data['file']

        new_video = Video()
        new_video.videofile.save(f.name, f, save=True)
        new_video.save()

        if not os.path.exists("/outside_docker/input/videos/"):
            os.makedirs("/outside_docker/input/videos/")
        shutil.move(new_video.videofile.path, "/outside_docker/input/" + new_video.videofile.name)

        return Response(data={"video_id": new_video.pk}, status=201)


def define_if_pose_is_ok(json_output_path, video):
    f = []
    for (dirpath, dirnames, filenames) in os.walk(json_output_path):
        f.extend(filenames)
    f.sort()

    def find_the_most_probable_point(array):
        x, y, probability = None, None, 0
        for i in range(0, len(array), 3):
            if probability < array[i + 2]:
                x = array[i]
                y = array[i + 1]
                probability = array[i + 2]
        return x, y

    max_vertical_hand_diviations = []
    max_horisontal_hand_diviations = []

    max_vertical_spine_diviations = []
    max_horisontal_spine_diviations = []

    for file in f:
        with open(json_output_path + file, encoding='utf-8') as data_file:
            if ".json" in file:
                data = json.loads(data_file.read())
                for person in data['part_candidates']:
                    x_5, y_5 = find_the_most_probable_point(person['5'])
                    x_6, y_6 = find_the_most_probable_point(person['6'])
                    x_7, y_7 = find_the_most_probable_point(person['7'])

                    if None not in [x_5, y_5, x_6, y_6, x_7, y_7]:
                        max_vertical_hand_diviation = max(abs(x_5 - x_6), abs(x_5 - x_7), abs(x_7 - x_6))
                        max_horisontal_hand_diviation = max(abs(y_5 - y_6), abs(y_5 - y_7), abs(y_7 - y_6))

                        max_vertical_hand_diviations.append(max_vertical_hand_diviation)
                        max_horisontal_hand_diviations.append(max_horisontal_hand_diviation)

                    x_1, y_1 = find_the_most_probable_point(person['1'])
                    x_8, y_8 = find_the_most_probable_point(person['8'])

                    if None not in [x_1, y_1, x_8, y_8]:
                        max_vertical_spine_diviation = abs(x_1 - x_8)
                        max_horisontal_spine_diviation = abs(y_1 - y_8)

                        max_vertical_spine_diviations.append(max_vertical_spine_diviation)
                        max_horisontal_spine_diviations.append(max_horisontal_spine_diviation)

    if len(max_horisontal_hand_diviations) > 0 and len(max_vertical_hand_diviations) > 0:
        if np.percentile(np.array(max_horisontal_hand_diviations), 95) / np.percentile(np.array(max_vertical_hand_diviations), 95) < 2.5:
            mistake = Mistake(title=Mistake.MISTAKE_WRONG_HAND_ANGLE)
            mistake.video = video
            mistake.save()

    if len(max_vertical_spine_diviations) > 0 and len(max_horisontal_spine_diviations) > 0:
        if np.percentile(np.array(max_vertical_spine_diviations), 95)/np.percentile(np.array(max_horisontal_spine_diviations), 95) < 6:
            mistake = Mistake(title=Mistake.MISTAKE_WRONG_SPINE_ANGLE)
            mistake.video = video
            mistake.save()


class VideoView(viewsets.ModelViewSet):
    queryset = Video.objects.all()
    serializer_class = VideoSerializer

    def list(self, *args, **kwargs):
        # define_if_pose_is_ok("/Users/motakuk/Desktop/Junction/output/31/", Video.objects.get(pk=31))
        # define_if_pose_is_ok("/Users/motakuk/Desktop/Junction/output/32/", Video.objects.get(pk=32))
        return super().list(*args, **kwargs)
