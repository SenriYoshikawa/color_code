from PIL import Image 
import qrcode
import sys
import numpy as np
import subprocess

args = sys.argv

np_imgs = []
result = []

np_imgs.append(Image.open(args[1]))
size = np_imgs[0].size
for i in range(3):
	np_imgs.append(Image.new('L',size))

for x in range(size[0]):
	for y in range(size[1]):
		r,g,b = np_imgs[0].getpixel((x,y))
		np_imgs[1].putpixel((x, y),(int(0.275 * r + 0.504 * g + 0.098 * b + 16)))
		np_imgs[2].putpixel((x, y),(int(-0.148 * r - 0.291 * g + 0.439 * b + 128)))
		np_imgs[3].putpixel((x, y),(int(0.439 * r - 0.368 * g - 0.071 * b + 128)))

for i in range(3):
	name = str(i) + '.png'
	np_imgs[i + 1].save(name)
	result.append(subprocess.check_output('zbarimg -q ' + name, shell = True).decode(sys.stdin.encoding)[8:])
	subprocess.call('rm ' + name, shell = True)
	print(result[i])

