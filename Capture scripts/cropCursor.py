import cv2
import numpy as np
import glob
import time

templ_color = cv2.imread('cursor1.jpg', cv2.IMREAD_COLOR)
mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)

templ = cv2.imread('cursor1.jpg', cv2.IMREAD_GRAYSCALE)
mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)
use_mask = True
match_method = cv2.TM_SQDIFF

w, h = templ.shape[:2]
crop_size = 150
crop_mid = int(crop_size/2)
file_nr = 1

for filepath in glob.iglob('tl/*.jpg'):
	# print("processing: "+filepath)
	img_color = cv2.imread(filepath, cv2.IMREAD_COLOR)
	img = cv2.imread(filepath, cv2.IMREAD_GRAYSCALE)

	startTime = time.time()
	method_accepts_mask = (cv2.TM_SQDIFF == match_method or match_method == cv2.TM_CCORR_NORMED)
	if (use_mask and method_accepts_mask):
		result = cv2.matchTemplate(img, templ, match_method, None, mask)
	else:
		result = cv2.matchTemplate(img, templ, match_method)

	cv2.normalize(result, result, 0, 1, cv2.NORM_MINMAX, -1)
	minVal, maxVal, minLoc, maxLoc = cv2.minMaxLoc(result, None)

	if (match_method == cv2.TM_SQDIFF or match_method == cv2.TM_SQDIFF_NORMED):
		matchLoc = minLoc
	else:
		matchLoc = maxLoc

	# get the cropped image: set -+ points and dst name
	y1 = matchLoc[1] - crop_mid
	y2 = matchLoc[1] + crop_mid
	x1 = matchLoc[0] - crop_mid
	x2 = matchLoc[0] + crop_mid
	crop_img = img[y1:y2, x1:x2]

	cv2.imwrite("tl/c"+str(crop_size)+"/"+str(file_nr)+".jpg", crop_img)
	file_nr = file_nr + 1


cv2.destroyAllWindows()
