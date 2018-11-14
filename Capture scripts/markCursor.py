import cv2
import numpy as np
import glob

templ = cv2.imread('cursor1.jpg', cv2.IMREAD_GRAYSCALE)
mask = cv2.imread('mask3.jpg', cv2.IMREAD_GRAYSCALE)
use_mask = True
match_method = cv2.TM_SQDIFF
# match_method = cv2.TM_CCORR_NORMED

w, h = templ.shape[:2]

cv2.namedWindow('Output')
cv2.moveWindow('Output', 0, 0)

for filepath in glob.iglob('tl/*1.jpg'):
	img = cv2.imread(filepath, cv2.IMREAD_GRAYSCALE)

	method_accepts_mask = (cv2.TM_SQDIFF == match_method or match_method == cv2.TM_CCORR_NORMED)
	if (use_mask and method_accepts_mask):
		result = cv2.matchTemplate(img, templ, match_method, None, mask)
	else:
		result = cv2.matchTemplate(img, templ, match_method)

	cv2.normalize( result, result, 0, 1, cv2.NORM_MINMAX, -1)
	minVal, maxVal, minLoc, maxLoc = cv2.minMaxLoc(result, None)

	if (match_method == cv2.TM_SQDIFF or match_method == cv2.TM_SQDIFF_NORMED):
		matchLoc = minLoc

		# threshold = 0.02
		# loc = np.where( result <= threshold)
	else:
		matchLoc = maxLoc

		# threshold = 0.98
		# loc = np.where( result >= threshold)

	# for pt in zip(*loc[::-1]):
		# cv2.rectangle(img, pt, (pt[0] + h, pt[1] + w), (0,255,255), 2, 8, 0)
	cv2.rectangle(img, matchLoc, (matchLoc[0] + 12, matchLoc[1] + 20), (0,255,255), 2, 8, 0 )

	cv2.imshow('Output',img)

	cv2.waitKey(0)

cv2.destroyAllWindows()
