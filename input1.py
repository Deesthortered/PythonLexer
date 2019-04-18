
'234'.ur"a h tebe"
sdf'13'

""" h bl""".'ska'
,.#<;

# comment1

def BasicOpportunities():
    print("Printim chto popalo", "i daze bolshe", 1234567890) # some comment 1

    a = input("VVedite pervoe znachenie: ")
    b = input("Vvedite vtotoe znachenie: ")

    print("Concat: a + b = ", a+b)
    
    a = int(a)
    b = float(b)

    print("a+b", a+b)
    print("a-b", a-b)
    print("a*b", a*b)
    print("a/b", a/b)
    print("a//b", a//b)
    print("a%b", a%b)
    print("a**b", a**b)

    a += 1
    print("a++", a)

    print("A teper razberem vetvlenia")
    a = int(input("Vvedite chislo: "))
    if a > 0:
        print("+")
    elif a < 0:
        print("-")
    else:
        print("0")

    print("A teper cicly")
    b = input("Vvedite slovo: ")
    for i in b:
        print(i*3) # tri raza

    a = int(input("A teper vvedite chislo: "))
    i = 0
    while i < a and i < 10:
        print(b)
        i += 1

    a = True
    b = False
    if not (a < 5 and b < 3) or a == b:
        print("blabla")

    a = [1, 2, 3]
    b = [1, 2, 3]
    if a is b:
        print("true")
    else:
        print("false")

    _ = 123
    print(_)

    pass