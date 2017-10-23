#!/bin/python

import urllib2
import sys
import os

key = os.environ.get('NEWSAPI_KEY')
if (key == ""):
  exit()
else:
  url = "https://newsapi.org/v1/articles?apiKey=" + key + "&sortBy=latest&source="
  source=["abc-news-au","al-jazeera-english","ars-technica","associated-press","bbc-news","bbc-sport","bloomberg","breitbart-news","business-insider","business-insider-uk","buzzfeed","cnbc","cnn","daily-mail","engadget","entertainment-weekly","espn","espn-cric-info","financial-times","football-italia","fortune","four-four-two","fox-sports","google-news","hacker-news","ign","independent","mashable","metro","mirror","mtv-news","mtv-news-uk","national-geographic","new-scientist","newsweek","new-york-magazine","nfl-news","polygon","recode","reddit-r-all","reuters","talksport","techcrunch","techradar","the-economist","the-guardian-au","the-guardian-uk","the-hindu","the-huffington-post","the-lad-bible","the-new-york-times","the-next-web","the-sport-bible","the-telegraph","the-times-of-india","the-verge","the-wall-street-journal","the-washington-post","time","usa-today"]

  for s in source:
    if (len(sys.argv) == 1):
      exit()
    else:
      file_to_open = sys.argv[1] + s
    file = open(file_to_open + ".json", "w")
    print url + s
    try:
      file.write( urllib2.urlopen("{}{}".format(url, s)).read() )
    except:
      newUrl = url.replace("&sortBy=latest", "")
      file.write( urllib2.urlopen("{}{}".format(newUrl, s)).read() )
    finally:
      file.close()