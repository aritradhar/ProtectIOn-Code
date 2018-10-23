from time import sleep
from picamera import PiCamera

camera = PiCamera()
camera.resolution = (1280, 720)
try:
    camera.start_preview()
    sleep(2)
    print("Camera warm up complete")
    for filename in camera.capture_continuous('tl/img{counter:03d}.jpg'):
        print('Captured %s' % filename)
        #sleep(0.01)
finally:
    camera.stop_preview()
    camera.close()
    print("Camera stopped")