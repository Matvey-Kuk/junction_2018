from rest_framework import serializers

from catssports.models import Video, Mistake


class MistakeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Mistake
        fields = ('title', )


class VideoSerializer(serializers.ModelSerializer):
    mistakes = MistakeSerializer(many=True)

    class Meta:
        model = Video
        fields = ('id', 'processed', 'people', 'rendered_video', 'videofile', 'errored', 'mistakes')
