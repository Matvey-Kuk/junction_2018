# Generated by Django 2.1.2 on 2018-11-24 20:48

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('catssports', '0002_auto_20181124_1426'),
    ]

    operations = [
        migrations.AddField(
            model_name='video',
            name='people',
            field=models.IntegerField(default=0),
        ),
    ]
