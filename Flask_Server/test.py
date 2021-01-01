from PIL import Image
import pytesseract
import cv2 
import numpy as np


pytesseract.pytesseract.tesseract_cmd = '/usr/bin/tesseract'
img = cv2.imread(('test1.png'), 0)

nwidth = 4000
height, width = img.shape
ratio = float(nwidth)/width
nheight = int(height*ratio)
nimg = cv2.resize(img, (nwidth, nheight), interpolation=cv2.INTER_CUBIC)
text = pytesseract.image_to_string(nimg, lang='eng')
print(text)

kernel = np.ones((3,3), np.uint8)
erosion = cv2.erode(nimg, kernel, iterations = 1)
text = pytesseract.image_to_string(erosion, lang='eng')
print(text)

im = Image.open('test1.png')
ratio = float(nwidth)/im.size[0]
nheight = int(im.size[1]*ratio)
nim = im.resize( (nwidth, nheight), Image.BILINEAR )
text = pytesseract.image_to_string(nim, lang='eng')
print(text)

cv2.imwrite('erosion.png', erosion)
cv2.imwrite('nimg.png', nimg)

# text = pytesseract.image_to_string(img, lang='eng')
# print(text)
