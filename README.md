# HTextView
Animation effects with custom font support to TextView

![](https://img.shields.io/hexpm/l/plug.svg)
![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![](https://img.shields.io/badge/Android-CustomView-blue.svg)

see [iOS Effects](https://github.com/lexrus/LTMorphingLabel)

---

## Screenshot


| type  | gif |
| :-- | :-- |
| Scale     | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo3.gif) |
| Evaporate | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo5.gif) |
| Fall      | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo6.gif) |
| Line      | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo7.gif) |
| Typer     |  ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/typer.gif) |
| Rainbow   | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/rainbow.gif) |
| Fade      | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/fade.gif) |

## Usage



```gradle
String htextview_version = "0.1.1"

compile "com.hanks:htextview-base:$htextview_version"    // base library

compile "com.hanks:htextview-fade:$htextview_version"        // optional
compile "com.hanks:htextview-line:$htextview_version"        // optional
compile "com.hanks:htextview-rainbow:$htextview_version"     // optional
compile "com.hanks:htextview-fade:$htextview_version"        // optional
compile "com.hanks:htextview-typer:$htextview_version"       // optional

compile "com.hanks:htextview-scale:$htextview_version"       // optional
compile "com.hanks:htextview-evaporate:$htextview_version"   // optional
compile "com.hanks:htextview-fall:$htextview_version"        // optional
```


### line

```xml
<com.hanks.htextview.line.LineTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:gravity="right"
    android:paddingRight="10dp"
    android:text="This is LineTextView\nToday is Monday"
    android:textSize="16sp"
    app:animationDuration="3000"
    app:lineColor="#1367bc"
    app:lineWidth="4dp"/>
```

### fade

```xml
<com.hanks.htextview.fade.FadeTextView
    android:layout_width="240dp"
    android:layout_height="150dp"
    android:gravity="left"
    android:letterSpacing="0.08"
    android:lineSpacingMultiplier="1.3"
    android:text="This is FadeTextView"
    android:textColor="#fff"
    android:textSize="20sp"
    app:animationDuration="1500"/>
```

### typer

```xml
<com.hanks.htextview.typer.TyperTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="this is init sentence."
    app:charIncrease="3"
    app:typerSpeed="80"/>
```

### rainbow

```xml
<com.hanks.htextview.rainbow.RainbowTextView
    android:layout_width="120dp"
    android:layout_height="wrap_content"
    android:gravity="right"
    android:text="this is init sentence"
    android:textSize="20sp"
    app:colorSpace="150dp"
    app:colorSpeed="4dp"/>
```

### scale (single line)

```xml
<com.hanks.htextview.scale.ScaleTextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="this is init sentence"
    android:textSize="16sp"/>
```


### evaporate (single line)

```xml
<com.hanks.htextview.evaporate.EvaporateTextView
    android:layout_width="match_parent"
    android:layout_height="100dp"
    android:gravity="center"
    android:paddingTop="8dp"
    android:text="this is init sentence"
    android:textSize="20sp"/>
```

### fall  (single line)

```xml
<com.hanks.htextview.fall.FallTextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingBottom="20dp"
    android:text="this is init sentence"
    android:textSize="16sp"/>
```

## License

This library is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

See [`LICENSE`](LICENSE) for full of the license text.

    Copyright (C) 2015 [Hanks](https://github.com/hanks-zyh)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
