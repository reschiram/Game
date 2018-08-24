import socket
import sys
import time
from _thread import *
from multiprocessing import Queue
from random import *
q = Queue()
bq = Queue()

host = ''
port = 12345
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
   s.bind((host, port))
except socket.error as e:
   print(str(e))
print('Warte auf client')
s.listen(5)

class fakeQueue:
    qu = []
    def put(a):
        print(qu)
        qu.append(a)
    def get(self, name):
        for item in self.qu:
            a, b = item.split()
            if a == name:
                return b
                break
        return '0'

def client_thread(conn, q, bq, fq, name):
    start_new_thread(thread_back, (conn, q, bq, fq, name))
    print('neu:', name)
    while True:
        ##recieve
        data = conn.recv(1024)
        data = data.decode('utf-8')
        q.put(data)
        ##send
        #for item in list(bq.queue) oder
        #for item in 
        ##break(disabled)
        #if not data:
        #   break
    conn.close()

def thread_back(conn, q, bq, fq, name):
    print(name)
    conn.send(str.encode('Hallo, i bims da soerver:'))
    while True:
        text  = fq.get(name)
        if text != '0':
            conn.send(str.encode(text))
        data2 = str('dinge')
        conn.send(str.encode(data2))

def gesamt_verwerter(q, bq, fq):
    list = open('list.txt', 'a')
    while True:
        if q.full() == 'true':
            #ueberlastet
            print('a')
        else:
            text = q.get()
            print(text)
            list.write(text)
            #auswerten
    #alle rueckmeldungen in bq
    list.close()
            
list = open("list.txt", "w")
list.close()

nl = [] #namensliste
fq = fakeQueue()
start_new_thread(gesamt_verwerter, (q, bq, fq))
while True:
   conn, addr = s.accept()
   print('gefunden: ', addr[0]+':'+str(addr[1]))
   a = 0
   while a == 0:
       name = randint(1001, 9999)
       a = 1
       for item in nl:
           if name == item:
               a = 0
               break
   nl.append(name)
   #name als neuer name
   start_new_thread(client_thread, (conn, q, bq, fq, name))
