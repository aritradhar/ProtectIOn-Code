from time import sleep
from picamera import PiCamera

camera = PiCamera()
camera.resolution = (1920, 1080)
try:
    camera.start_preview()
# Camera warm-up time
    sleep(2)
    camera.capture('foo.jpg')
    print("Done..")
finally:
    camera.stop_preview()
    camera.close()
    print("Camera stopped")