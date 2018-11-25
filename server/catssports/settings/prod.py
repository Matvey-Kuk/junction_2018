import os

from .base import *  # noqa

CELERY_BROKER_URL = 'redis://redis'

ALLOWED_HOSTS = ['35.185.199.57', 'localhost']


DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': '/db/db.sqlite3',
    }
}

print("DB location:{}".format(DATABASES['default']['NAME']))
print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")