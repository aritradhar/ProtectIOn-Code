import pyscreenshot as ImageGrab
from Xlib import display
import matplotlib.pyplot as plt
import matplotlib.patches as patches
from PIL import Image
import numpy as np

data = display.Display().screen().root.query_pointer()._data
#img = np.array(Image.open('tl/img001.jpg'), dtype=np.uint8)

#data["root_x"], data["root_y"]
x = data["root_x"]
y = data["root_y"]
# fullscreen
#im = ImageGrab.grab(bbox=(1000,1000, 0, 0))  # X1,Y1,X2,Y2
im = ImageGrab.grab()
rect = patches.Rectangle((x,y),40,30,linewidth=2,edgecolor='r',facecolor='none')
print(x)
print(y)
fig,ax = plt.subplots(1)
ax.imshow(im)
ax.add_patch(rect)
plt.show()
#im.show()
