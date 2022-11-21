from faker import Faker
import json
import time, random


def myrandom(mini, maxi):
    random.seed(time.time())
    val = random.randint(mini, maxi)
    return val


class DataGen:
    def __init__(self):
        self.fake = Faker()
        # f = open('../../data/mapping.json', "r")
        # self.ads_to_camp = json.loads(f.read())
        # f.close()
        f = open('../data/ad_ids.txt', "r")
        self.ads = []
        for ad in f:
            self.ads.append(ad.strip())
        f.close()

    def get_click_event(self):
        ad_id = self.ads[myrandom(0, 999)]
        # c_id = self.ads_to_camp[ad_id]
        events = ["view", "click", "purchase"]
        event = events[myrandom(0, 2)]
        return {
            "ip_address": self.fake.ipv4_public(),
            "user_id": self.fake.uuid4(),
            "page_id": self.fake.uuid4(),
            "ad_id": ad_id,
            "event_type": event
        }
