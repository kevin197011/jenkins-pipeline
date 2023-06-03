

def a = 'https://github.com/kevin197011/chatOps.git'

def last = a.split('/').last().toString().tokenize('.').first().toString()

println(last)