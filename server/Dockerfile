FROM python:3.7-alpine

RUN apk add python3-dev build-base linux-headers pcre-dev docker ffmpeg
RUN pip install uwsgi

WORKDIR /etc/app
COPY ./requirements.txt ./
RUN pip install -r requirements.txt

COPY ./catssports ./catssports
RUN rm ./catssports/db.sqlite3
COPY ./manage.py ./manage.py
RUN python manage.py collectstatic --no-input
