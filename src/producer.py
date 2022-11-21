#!../env/bin/python3
from kafka import KafkaProducer
import json
from data_gen import DataGen
import time


def json_serializer(data):
    return json.dumps(data).encode('utf-8')


producer = KafkaProducer(bootstrap_servers=['localhost:9092'], value_serializer=json_serializer)

if __name__ == "__main__":
    dg = DataGen()
    while True:
        ad_click = dg.get_click_event()
        # print([type(value) for value in ad_click.values()])
        print(ad_click)
        producer.send("multiquery", ad_click)
        #time.sleep(2)
