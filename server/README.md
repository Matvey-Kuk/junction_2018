

```
docker build -t openpose .

sudo docker run --runtime=nvidia -v /home/ubuntu/data:/data -it openpose:latest bash -c 'cd /opt/openpose-master; ./build/examples/openpose/openpose.bin --render_pose 0 --video /data/input/test1.mov --display 0 -write_json /data/output/json'

celery -A catssports worker -l info
```


```
rsync -r /Users/motakuk/Desktop/Junction/server/* motakuk@35.185.199.57:/opt/catssport/
ssh motakuk@35.185.199.57 bash -c "cd /opt/catssport/; docker-compose build /opt/catssport/"
```