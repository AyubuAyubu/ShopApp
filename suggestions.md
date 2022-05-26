The code is well writen however as with any project there is always room for improvement.

# Git workflows
From what I saw only one branch exists (main) and there were no pull requests. This works well when you are the only person working on a project.
Have a look at git workflows: This will help you coordinate better in a team: https://nvie.com/posts/a-successful-git-branching-model/
There are other git workflows. The one mentioned above is a good starting point.

# App Architecture
Consider using MVVM or MVI or MVP architechture,
This is because from the Android community they are the most widely used Android app architechures.
It will also make the code more maintainable and testable.
MVVM is a good starrting point considering it is recommended by google and has good documentation.
After implementing MVVM I suggest having a crack at MVI on your next project; it is an addition to the MVVM architecture

Consider using a single activity with multiple fragments
why? https://proandroiddev.com/what-problems-exist-with-multi-activities-4ea1a335d85a
https://www.reddit.com/r/androiddev/comments/8of7s8/what_problems_exist_with_multi_activities/

Last we spoke we had touched on Dependency Injection
There are two candidates that come to mind:
Koin: https://insert-koin.io/
Dagger hilt: https://dagger.dev/hilt/

# Useful resources:
Google code labs: https://codelabs.developers.google.com/?product=android  You can use this to learn a particular topic before implementing it.

ProAndroid dev: https://proandroiddev.com/

A good starting point for reference, feel free to adapt it as you please: https://github.com/odaridavid/Clean-MVVM-ArchComponents

> Your misison should you choose to accept it
> is to make a killer app!
