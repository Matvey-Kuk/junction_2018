from __future__ import absolute_import, unicode_literals

import os

from celery import Celery
from django.conf import settings
from django.apps import apps
from subprocess import Popen, PIPE
from django.core.files import File

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'catssports.settings.dev')

app = Celery('catssports')

app.config_from_object('django.conf:settings', namespace='CELERY')
app.autodiscover_tasks(lambda: settings.INSTALLED_APPS)


@app.task(bind=True, name='process_one_video')
def process_one_video(self):
    Video = apps.get_model("catssports", "Video")
    unprocessed_queryset = Video.objects.filter(processed=False, errored=False)
    print("Queue: {}".format(unprocessed_queryset.count()))
    video = unprocessed_queryset.last()
    if video is not None:
        try:
            print("Wohooo, rendering video!")
            if not os.path.exists("/outside_docker/output/" + str(video.pk) + "/json"):
                os.makedirs("/outside_docker/output/" + str(video.pk) + "/json")

            video_output_name = "/outside_docker/output/" + str(video.pk) + "/video.avi"
            video_output_name_mp4 = "/outside_docker/output/" + str(video.pk) + "/video.mp4"
            json_output_path = "/outside_docker/output/" + str(video.pk) + "/json"
            command = "docker run --runtime=nvidia -v /outside_docker:/outside_docker openpose:latest bash -c 'cd /opt/openpose-master; ./build/examples/openpose/openpose.bin --part_candidates --video " + "/outside_docker/input/" + video.videofile.name + " --display 0 --write_video " + video_output_name + " -write_json " + json_output_path + "'"
            process = Popen(command, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
            stdout, stderr = process.communicate()
            process.wait()
            print(stdout)
            print(stderr)

            # Converting avi to mp4
            command = "ffmpeg -i " + video_output_name + " -vf \"transpose=1\" " + video_output_name_mp4
            process = Popen(command, shell=True, stdin=PIPE, stdout=PIPE, stderr=PIPE)
            stdout, stderr = process.communicate()
            process.wait()
            print(stdout)
            print(stderr)

            max_people = 0
            video.people = max_people

            video_output_file = open(video_output_name_mp4, 'rb')
            video.rendered_video.save("rendered.mp4", File(video_output_file))
            video.processed = True

            from catssports.views import define_if_pose_is_ok
            define_if_pose_is_ok(json_output_path + "/", video)

        except Exception as e:
            video.errored = True
            raise e
        finally:
            video.save()
