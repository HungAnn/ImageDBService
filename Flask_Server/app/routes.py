from flask import Flask, redirect, url_for, request
from app import app
from PIL import Image
import pytesseract
import json
import os

pytesseract.pytesseract.tesseract_cmd = '/usr/bin/tesseract'

def img2string(img):
    width = 5000
    ratio = float(width)/img.size[0]
    height = int(img.size[1]*ratio)
    nim = img.resize( (width, height), Image.BILINEAR )
    text = pytesseract.image_to_string(nim, lang='eng')

    return text

@app.route('/', methods=['GET', 'POST'])
def index():
    download_folder = './catch'
    if request.method == 'POST':
        
        data = request.files['uploadFile']
        data.save(os.path.join(download_folder, 'output.png'))
        
        img = Image.open((os.path.join(download_folder, 'output.png')))
        text = img2string(img)
        
        print('===================================')
        print('Image name: '+request.form['image_name'])
        print('Result:')
        print(text)
	
    return text
