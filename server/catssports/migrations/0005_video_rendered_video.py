# Generated by Django 2.1.2 on 2018-11-25 00:57

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('catssports', '0004_auto_20181124_2255'),
    ]

    operations = [
        migrations.AddField(
            model_name='video',
            name='rendered_video',
            field=models.FileField(default=None, null=True, upload_to='videos'),
        ),
    ]