from background_task import background
from django.contrib.auth.models import User
import datetime
from .models import Order, Profile, Drone, User, Store, Product

@background(schedule=2)
def Drone_Function(droneName,orderName,username):
    var=0
    getDrone = Drone.objects.get(droneName=droneName)
    getUser = User.objects.get(username=username)
    getProfile = Profile.objects.get(user=getUser)
    getOrder = Order.objects.get(orderName=orderName)
    droneCoord = getDrone.currentCoord.split(',')
    storeCoord = getDrone.storeCoord.split(',')
    userCorod = getDrone.destinationCoord.split(',')
    droneX=int(droneCoord[0])
    droneY=int(droneCoord[1])
    userX=int(userCorod[0])
    userY=int(userCorod[1])
    storeX=int(storeCoord[0])
    storeY=int(storeCoord[1])
    ## we need to check for payment here while not pay no progress
    while(getOrder.status != "1"):
        getOrder = Order.objects.get(orderName=orderName)


    while((droneX!=storeX) or (droneY!=storeY)):
        t1 = datetime.datetime.now()
        t2 = datetime.datetime.now()
        delta = t2 - t1
        # other stuff here

        while(delta.seconds<1):
            t2 = datetime.datetime.now()
            delta = t2 - t1

        if (droneX > storeX):
            droneX-=1
        if (droneY > storeY):
            droneY -= 1
        if (droneX < storeX):
            droneX += 1
        if (droneY < storeY):
            droneY += 1
        print(str(droneX)+","+str(droneY))
        getDrone.currentCoord = str(droneX)+","+str(droneY)
        getDrone.save()
        getOrder.status = 2
        getOrder.save()


    while ((droneX != userX) or (droneY != userY)):
        t1 = datetime.datetime.now()
        t2 = datetime.datetime.now()
        delta = t2 - t1
        # other stuff here

        while (delta.seconds < 1):
            t2 = datetime.datetime.now()
            delta = t2 - t1

        if (droneX > userX):
            droneX -= 1
        if (droneY > userY):
            droneY -= 1
        if (droneX < userX):
            droneX += 1
        if (droneY < userY):
            droneY += 1
        print(str(droneX) + "," + str(droneY))
        getDrone.currentCoord = str(droneX) + "," + str(droneY)
        getDrone.save()
    while (getOrder.status != "3"):
        getOrder = Order.objects.get(orderName=orderName)
    getDrone.order = None
    getDrone.currentCoord = "434723,805449"
    getDrone.save()
