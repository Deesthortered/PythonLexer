
def ListOperations():
    l = [1, 2, "3", [4], [5, 6, 7]] # comment1
    print(l)                        # comment2
    print("\n")

    # comment3
    for i in l:
        print(i)
    print("\n")

    print(l[1:3])

    print(l[1:-1:2])

    l[0:2] = [-1, -1, -1]
    print(l)
    print("\n")

    print(l[::2])
    print("\n")

    l1 = l[:]
    l1 = list(l)

    l = [1, 2, 3]
    print(" l = ", l)
    print("l1 = ", l1)
    print("\n")

    l = l1
    print(" l = ", l)
    print("l1 = ", l1)
    print("\n")


    l =  [1, 2, 3, 4]
    l1 = [3, 4, 5, 6]
    print(l + l1)
    print(l * 2)

    for x in l:
        pass
    for x in sorted(l):
        pass
    for x in set(l):
        pass
    for x in reversed(l):
        pass
    for x in set(l).difference(l1):
        pass

    l.sort()
    len(l)
    l.append(1)
    l.index(1, 0, 3)
    l.insert(0, 1)
    l.count(1)
    l.remove(1)
    l.reverse()
    l.pop(0)
    del l[1]
    max(l)
    min(l)
    if 100 in l:
        print("Yes")
    else:
        print("No")

    l  = [ i*i for i in range(1,10)]
    l1 = [ i*i for i in range(1,10) if i % 2 == 0]

    pass

def TupleOperations():
    t = (1, 2, 3)
    t = 1, 2, 3

    t = (1, 2, [3, 4])
    print(t)
    t[2].append(5)
    print(t)

    print(tuple("qwerty"))

    b = True
    if b == True:
        pass
    b = True;