# seesaw-view


A view container that gives visual feedback by sessawing a view.

## Basic idea

Since flat views like `ImageView` with a click-handler do not give visual feedback when user clicks on them, the seesaw-view provides a container, that seesaws the view „into the screen“ where the user touches it.

## Background

The view is a plane that is on an imaginary stick as seen on this illustration.

![seesaw-view](seesaw-view.png)



## Get it
In your root `build.gradle` at the end of repositories

```groovy
    	allprojects {
    		repositories {
    			...
    			maven { url 'https://jitpack.io' }
    		}
    	}
```

Add the dependency

```groovy
    dependencies {
    	        implementation 'com.github.hayribakici:seesaw-view:0.1.1'
    	}
```