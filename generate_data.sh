#!/bin/bash

cd "${NEWSINGESTION_ROOT}"
DATE=`date +%m_%d`
storage_dir="local_datastore"

if [ ! -d "${storage_dir}" ]; then
    mkdir "${storage_dir}"
fi

dest="${storage_dir}/${DATE}"

if [ ! -d "${dest}" ]; then
	mkdir "${dest}"
	python run.py "${dest}/"

    if [ "$?" != "0" ]; then
        echo "Run script did not complete. Exiting"
        exit 3
    fi

	aws s3 sync "${dest}" "s3://news.ingestion.project/${DATE}/"

	if [ "$?" = "0" ]; then
	    rm -rf ${dest}
	fi
fi
