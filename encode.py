from PIL import Image 
import qrcode
import sys
import numpy as np

_y1 = 60
_y2 = 160
_b1 = 102
_b2 = 170
_r1 = 97
_r2 = 170

args = sys.argv

np_imgs = []

for arg in args[1:4]:
	np_imgs.append(qrcode.make(arg))
	#imgs[-1].show()

size = np_imgs[0].size
qrimg = Image.new('RGB',size)

for x in range(size[0]):
	for y in range(size[1]):
		yy = _y2 if np_imgs[0].getpixel((x,y)) > 128 else _y1
		cb = _b2 if np_imgs[1].getpixel((x,y)) > 128 else _b1
		cr = _r2 if np_imgs[2].getpixel((x,y)) > 128 else _r1

		r = 1.164 * (yy - 16) + 1.596 * (cr - 128)
		g = 1.164 * (yy - 16) - 0.391 * (cb - 128) - 0.813 * (cr - 128)
		b = 1.164 * (yy - 16) + 2.018 * (cb - 128)

		qrimg.putpixel((x, y),(int(r), int(g), int(b)))

qrimg.show()

