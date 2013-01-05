all:
	cd tests && ant clean && ant debug install && adb shell "am start -n hongbosb.rollingball/.MainActivity"
#run:
	#adb shell "pkill hongbosb" && adb shell "am start -n hongbosb.rollingball/.MainActivity"

