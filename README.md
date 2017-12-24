# ArcChartViewDemo
You can use this library to draw Arc charts

and show your statistics or anything you want

or maybe get some ratings from user.

you can download the [Demo apk file](./repo_files/ArcChartViewDemo.apk) (you can first adjust your Chart in the app and then implement it in code)

<img src="http://ikhoshabi.com/ss/gif/ArcChartView2.gif" width="300">



## 1 - Getting Started

By this instructions you can add this library and i will explain how use it.



### Add Maven to your root build.gradle

First of all Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

### Add Dependency

Add the dependency to your app build.gradle file

```
dependencies
{
    compile 'com.github.imaNNeoFighT:ArcChartView:-SNAPSHOT'
}
```

And then sync your gradle and take a tea.


## 2 - About The View
You can simply use this View like other Views in android,
just add ``ArcChartView`` in your java code or xml.

## View Properties 

### * in XML
you can use this properties for make everything you want,
and all of them is arbitary,and can change via xml or java code/

* `acv_lines_count`
  by this property you can specify lines count of chart (i mean arc lines), default value is `10`

* `acv_lines_space`
  by this property you can specify lines space (lines margin), default value is `4dp`
  
* `acv_lines_width`
  by this property you can specify lines width , default value is `6dp`

* `acv_sections_count`
  by this property you can specify sections count , default value is `8`

* `acv_sections_space`
  by this property you can specify sections space (sections margin) , default value is `4dp`

* `acv_mid_start_extra_offset`
  by this property you can specify center extra offest size, default value is `16dp`

* `acv_icon_size`
  by this property you can specify the icons size, default value is `32dp`


### * in Code
```
//acv_lines_count
myArcChartView.linesCount = 10
  
//acv_lines_space
myArcChartView.linesSpace = DpHandler.dpToPx(ctx,4) 

//acv_lines_width
myArcChartView.linesWidth = DpHandler.dpToPx(ctx,6)
 
//acv_sections_count
myArcChartView.sectionsCount = 8 

//acv_sections_space
myArcChartView.sectionsSpace = DpHandler.dpToPx(ctx,2) 

//acv_mid_start_extra_offset
myArcChartView.midStartExtraOffset = DpHandler.dpToPx(ctx,16)
 
//acv_icon_size
myArcChartView.iconSize = DpHandler.dpToPx(ctx,32) 
```


## set and get Sections value
keep in mind that sections position starts with 0

to get a section value use this function
```
value = myArcChartView.getSectionValue(sectionPos)
```



and to set a section value use this function
```
myArcChartView.getSectionValue(sectionPos,sectionValue)
```





## change filled and unFilled colors
to set the unFilled color (the section color that drawn behind) use this function
```
myArcChartView.setFilldeColor(sectionPos, Color.BLACK)
```

and to set the filled color (the section color that drawn in top) use this function
```
myArcChartView.setUnFilldeColor(sectionPos,Color.LTGRAY)
```

<img src="./repo_files/images/sample_change_color.png" width="80">





## change section icons
to set the icons use this function
```
myArcChartView.setIcon(sectionPos,bitmap)
```

<img src="./repo_files/images/sample_icons.png" width="300">





## 3 - Some Samples


### Sample 1

```
    <com.neo.arcchartview.ArcChartView
        android:id="@+id/arc_chart_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toTopOf="@+id/view_separator"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:acv_lines_count="10"
        app:acv_lines_width="6dp"
        app:acv_lines_space="4dp"
        app:acv_sections_count="8"
        app:acv_sections_space="2dp"
        app:acv_icon_size="32dp"
        app:acv_mid_start_extra_offset="12dp"
        />
```
<img src="./repo_files/images/sample1.png" width="300">




### Sample 2

```
    <com.neo.arcchartview.ArcChartView
        android:id="@+id/arc_chart_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toTopOf="@+id/view_separator"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:acv_lines_count="4"
        app:acv_lines_width="17dp"
        app:acv_lines_space="2dp"
        app:acv_sections_count="3"
        app:acv_sections_space="6dp"
        app:acv_icon_size="36dp"
        app:acv_mid_start_extra_offset="0dp"
        />
```
<img src="./repo_files/images/sample2.png" width="300">




### Sample 3

```
    <com.neo.arcchartview.ArcChartView
        android:id="@+id/arc_chart_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toTopOf="@+id/view_separator"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:acv_lines_count="6"
        app:acv_lines_width="14dp"
        app:acv_lines_space="0dp"
        app:acv_sections_count="18"
        app:acv_sections_space="0dp"
        app:acv_icon_size="14dp"
        app:acv_mid_start_extra_offset="8dp"
        />
```
<img src="./repo_files/images/sample3.png" width="300">

