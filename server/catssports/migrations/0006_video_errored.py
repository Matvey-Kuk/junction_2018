# Generated by Django 2.1.2 on 2018-11-25 02:40

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('catssports', '0005_video_rendered_video'),
    ]

    operations = [
        migrations.AddField(
            model_name='video',
            name='errored',
            field=models.BooleanField(default=False),
        ),
    ]