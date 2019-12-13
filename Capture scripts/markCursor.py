import cv2
import numpy as np
import glob
import time
import os

def get_match_confidence(img1, img2, mask=None):
	if img1.shape != img2.shape:
		return False

	thresh = 100
	if mask is not None:
		img1 = img1.copy()
		img1[mask==0] = 0
		# img1 = cv2.threshold(img1, thresh, 255, cv2.THRESH_BINARY)[1]
		# (thresh, img1) = cv2.threshold(img1, 128, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
		cv2.imshow('Cursor',img1)

		img2 = img2.copy()
		img2[mask==0] = 0
		# img2 = cv2.threshold(img2, thresh, 255, cv2.THRESH_BINARY)[1]
		# (thresh, img2) = cv2.threshold(img2, 128, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
		cv2.imshow('Template',img2)

	## using match
	match = cv2.matchTemplate(img1, img2, cv2.TM_CCOEFF)
	_, confidence, _, _ = cv2.minMaxLoc(match)
	# print confidence
	return confidence

templ_color = cv2.imread('cursor1.jpg', cv2.IMREAD_COLOR)
mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)

templ = cv2.imread('cursor1.jpg', cv2.IMREAD_GRAYSCALE)
mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)
use_mask = True
match_method = cv2.TM_SQDIFF
# match_method = cv2.TM_CCORR_NORMED

# black and white images
# templ = cv2.threshold(templ, thresh, 255, cv2.THRESH_BINARY)[1]
# mask = cv2.threshold(mask, thresh, 255, cv2.THRESH_BINARY)[1]
w, h = templ.shape[:2]

# cv2.namedWindow('Output')
# cv2.namedWindow('Cursor')
# cv2.namedWindow('Template')
# cv2.moveWindow('Output', 0, 0)

total = 0
ops = 0

for filepath in glob.iglob('tl/*.jpg'):
	print("processing: "+filepath)
	img_color = cv2.imread(filepath, cv2.IMREAD_COLOR)
	img = cv2.imread(filepath, cv2.IMREAD_GRAYSCALE)
	# img = cv2.threshold(img, thresh, 255, cv2.THRESH_BINARY)[1]

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
		# print("minVal: ", minVal)
		# threshold = 0.02
		# loc = np.where( result <= threshold)
	else:
		matchLoc = maxLoc
		# print("maxVal: ", maxVal)
		# threshold = 0.98
		# loc = np.where( result >= threshold)

	# for pt in zip(*loc[::-1]):
		# cv2.rectangle(img, pt, (pt[0] + h, pt[1] + w), (0,255,255), 2, 8, 0)

	# verify the found template

	cursor = img_color[matchLoc[1]: matchLoc[1] + 20, matchLoc[0]:matchLoc[0] + 12]
	# print("confidence: ", get_match_confidence(cursor, templ_color, mask))

	endTime = time.time()
	total = total + (endTime - startTime)
	ops = ops + 1
	# print(endTime - startTime)

	# cv2.imshow('cursor', cursor)
	# result = cv2.matchTemplate(cursor, templ, cv2.TM_CCOEFF)
	# minVal, maxVal, minLoc, maxLoc = cv2.minMaxLoc(result, None)
	# matchLoc = maxLoc
	# print("verify_maxVal: ", maxVal)

	cv2.rectangle(img_color, matchLoc, (matchLoc[0] + 12, matchLoc[1] + 20), (255,255,0), 2, 8, 0 )

	# cv2.imshow('Output', img)
	# cv2.waitKey(0)
	newFile = "tl/marked/{0}".format(os.path.basename(filepath))
	cv2.imwrite(newFile, img_color)

avg = total / ops
print("average: ", avg)
print("count: ", ops)
cv2.destroyAllWindows()
