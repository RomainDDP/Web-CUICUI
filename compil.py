import subprocess
import webbrowser
import platform

current_os = platform.system().lower()
commands = []
url = "http://localhost:8080"

webbrowser.open(url)

if current_os == "linux":
    commands = [
        "javac -d cls -cp lib/json.jar -sourcepath src src/cuicui/*.java",
        "java -cp cls:lib/json.jar cuicui.GenerationClesRSA",
    ]
elif current_os == "windows":
    commands = [
        "javac -d cls -cp lib\\json.jar -sourcepath src src\\cuicui\\*.java", 
        "java -cp \"cls;lib\\json.jar\" cuicui.GenerationClesRSA", 
    ]

for command in commands:
    subprocess.run(command, shell=True)

