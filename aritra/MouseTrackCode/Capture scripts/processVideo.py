import cv2
import numpy as np
import glob
import time
import os

mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)
templ = cv2.imread('cursor1.jpg', cv2.IMREAD_GRAYSCALE)

use_mask = True
match_method = cv2.TM_SQDIFF
w, h = templ.shape[:2]



vidcap = cv2.VideoCapture('screen_records/rec1.mp4')
success,image = vidcap.read()
count = 0

while success and count < 5:
    print("processing frame #:", count)
    grayImg = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    method_accepts_mask = (cv2.TM_SQDIFF == match_method or match_method == cv2.TM_CCORR_NORMED)

    if (use_mask and method_accepts_mask):
        result = cv2.matchTemplate(grayImg, templ, match_method, None, mask)
    else:
        result = cv2.matchTemplate(grayImg, templ, match_method)
    # result = cv2.matchTemplate(image, templ, match_method, None, mask)

    cv2.normalize(result, result, 0, 1, cv2.NORM_MINMAX, -1)
    minVal, maxVal, minLoc, maxLoc = cv2.minMaxLoc(result, None)

    if (match_method == cv2.TM_SQDIFF or match_method == cv2.TM_SQDIFF_NORMED):
        matchLoc = minLoc
    else:
        matchLoc = maxLoc

    cursor = image[matchLoc[1]: matchLoc[1] + 20, matchLoc[0]:matchLoc[0] + 12]

    cv2.rectangle(image, matchLoc, (matchLoc[0] + 12, matchLoc[1] + 20), (0,255,255), 2, 8, 0 )
    cv2.imwrite("screen_records/rec1/frame%d.jpg" % count, image)     # save frame as JPEG file

    success,image = vidcap.read()
    count += 1
