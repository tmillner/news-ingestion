#!/bin/python

import os
import requests
from time import sleep

url = "http://127.0.0.1:8080/api/external/persist/record"

def post_data(data):
	header = {"Content-Type": "application/json"}
	r = requests.post(url, data=data, headers=header)
	return r.text

for subdir in os.listdir("./"):
	abs_dir = "./" + subdir
	if(os.path.exists( abs_dir + "/")):
		for filename in os.listdir(abs_dir):
			file = os.path.join(abs_dir, filename)
			print(file)
			f = open(file, "r")
			data = f.read()
			f.close()
			print(post_data(data))
			sleep(0.5) # 1/2 Second
