import sys
import datetime
import imageio
import glob
import cv2

import os
import numpy as np
from PIL import Image, ImageFont, ImageDraw
import moviepy.editor as mpy


def create_gif(filenames, duration,path, gifName="animation"        ,fontSize =15,drawCounter=False,resize= 1,grayScale = False,blur = 0):
    images = []
    counter =0
    maxLength = len(filenames)
    font = ""

    #check which os for potential font writing
    #change this to a font location of your choice
    if os.name == 'nt':
        font = ImageFont.truetype("C:\\Windows\\WinSxS\\amd64_microsoft-windows-font-truetype-yugothic_31bf3856ad364e35_10.0.17134.1_none_be6df5bb828d2629\\YuGothR.ttc", 25)
    else:
        font = ImageFont.truetype("/Library/Fonts/Andale Mono.ttf", 15)

    #go through all files found with extension png/jpg
    for filename in filenames:
        #counter update used in drawing and for console progress
        counter = counter +1
        print (str(counter) + '/' +str(maxLength))
        img = Image.open(filename)
        width, height = img.size
        #change the size by original size * resize
        img = img.resize((width*resize,height*resize), Image.ANTIALIAS)

        #apply a gaus blur
        if blur >0:
            img = cv2.blur(np.array(img),(int(blur),int(blur)))

        #used to draw the loop counter
        if drawCounter == True:
            draw = ImageDraw.Draw(img)
            draw.text((0,0), str(counter) + '/' +str(maxLength), (255,0,0), font=font)




        #want to compare an image to your current gif? then use horizontal (h) / vertical (v) stack.
        #just load an image - JUST MAKE SURE TO RESIZE THE COMPARISON IMAGE
        #img = np.hstack((img,Image.open("comparisonImage.png").resize((width*resize,height*resize), Image.ANTIALIAS)))
        #img = np.vstack((img,Image.open("comparisonImage.png").resize((width*resize,height*resize), Image.ANTIALIAS)))


        #order matters - do this last to ensure colour channels aren't messed up
        #gif make needs 3 channel - so convert back to gray "RGB"
        if grayScale:
            img =  cv2.cvtColor(np.array(img),cv2.COLOR_RGB2GRAY)
            img =  cv2.cvtColor(np.array(img),cv2.COLOR_GRAY2RGB)


        img = np.array(img)
        images.append(img)


    fps = duration
    clip = mpy.ImageSequenceClip(images, fps=fps)
    clip.write_gif(path + '{}.gif'.format(gifName), fps=fps, loop = False)


if __name__ == "__main__":

    #------------------------MAIN GIF SETTINGS-----------------------------------
    types = ('*.jpg', '*.png','*eps')

    gifName="animation"

    grabEveryNFile=1
    reversableGif=False

    drawCounter=False
    fontSize =15

    resize= 1
    grayScale = False
    blur = 0
    #-----------------------------------------------------------------------------




    script = sys.argv.pop(0)
    #first arg is time between transition in miliseconds/ second arg is path to image location (glob will get all)
    if len(sys.argv) < 2:
        print('Usage: python {} <fps> <path to images separated by space>'.format(script))
        sys.exit(1)
    
    duration = float(sys.argv.pop(0))
    path = sys.argv.pop(0) 

    print(path)
    

    files_grabbed = []
    for ext in types:
        files_grabbed.extend(glob.glob(path + ext))


    #OPTINAL - IF FOR WHATEVER REASON YOUR FILES ARE NOT ORDERED BY DATE, 
    #USE THIS FUNCTION TO ORDER THEM BY A NUMERIC KEY 
    #ensure after splitting file name only a numeric value key is left
    def sortKey(s):
        print(s)
        return int(s.split('/')[1].split('.')[0].replace('foo',''))
    #files_grabbed.sort(key=sortKey) #advanced search key term

    print(files_grabbed)
    files_grabbed.sort(key=os.path.getmtime)

    #TOO MANY FILES? SIMPLY REMOVE SOME 
    files_grabbed = files_grabbed[::grabEveryNFile]
    print(files_grabbed)

    #WANT TO MAKE YOUR GIF REVERSE? (seemless transitions) - doubles size of gif..
    if reversableGif == True:
        files_grabbed.extend(files_grabbed[::-1])


    create_gif(files_grabbed, duration,path,
                gifName,
                fontSize ,
                drawCounter,
                resize,
                grayScale,
                blur)








