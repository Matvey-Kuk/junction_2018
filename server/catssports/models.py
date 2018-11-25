from django.db import models


class Video(models.Model):
    videofile = models.FileField(upload_to='videos')
    timestamp = models.DateTimeField(auto_now=True)
    processed = models.BooleanField(default=False)
    errored = models.BooleanField(default=False)
    people = models.IntegerField(default=0)
    rendered_video = models.FileField(upload_to='videos', null=True, default=None)


class Mistake(models.Model):
    MISTAKE_WRONG_HAND_ANGLE, MISTAKE_WRONG_SPINE_ANGLE = "wrong_hand_angle", "wrong_spine_angle"
    MISTAKES = (
        (MISTAKE_WRONG_HAND_ANGLE, 'wrong_hand_angle'),
        (MISTAKE_WRONG_SPINE_ANGLE, 'wrong_spine_angle'),
    )
    title = models.CharField(choices=MISTAKES, default=MISTAKE_WRONG_HAND_ANGLE, max_length=100)
    video = models.ForeignKey(
        Video,
        on_delete=models.CASCADE,
        null=True,
        default=None,
        related_name="mistakes"
    )
