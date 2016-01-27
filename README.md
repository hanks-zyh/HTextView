# HTextView
Animation effects to TextView

![](https://img.shields.io/hexpm/l/plug.svg)
![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![](https://img.shields.io/badge/Android-CustomView-blue.svg)

see [iOS Effects](https://github.com/lexrus/LTMorphingLabel)

---

## Screenshot
[Default : Scale](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/ScaleText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo3.gif)

[EvaporateText](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/EvaporateText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo5.gif)

[Fall](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/FallText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo6.gif)

[Line](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/LineText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo7.gif)

[Sparkle](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/SparkleText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo8.gif)

[Anvil](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/AnvilText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo2.gif)


[Typer](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/TyperText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/typer.gif)


[Rainbow](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/RainBowText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/rainbow.gif)

[Burn](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/BurnText.java)

![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/burn.gif)


## Usage

```groovy
compile 'hanks.xyz:htextview-library:0.1.3'
```

```xml
xmlns:htext="http://schemas.android.com/apk/res-auto"
```

```xml
<com.hanks.htextview.HTextView
       android:id="@+id/text"
       android:layout_width="match_parent"
       android:layout_height="100dp"
       android:background="#000000"
       android:gravity="center"
       android:textColor="#FFFFFF"
       android:textSize="30sp"
       htext:animateType="anvil"
       />
```

`animateType` can be `scale`   `evaporate`  `fall`  `sparkle`  `anvil`  `line` `pixelate` `typer` `rainbow` `burn`


```java
hTextView = (HTextView) findViewById(R.id.text);
hTextView.setAnimateType(HTextViewType.LINE);
hTextView.animateText("new simple string"); // animate
```


# Contact & Help

Please fell free to contact me if there is any problem when using the library.

- **email**: zhangyuhan2014@gmail.com
- **twitter**: https://twitter.com/zhangyuhan3030
- **weibo**: http://weibo.com/hanksZyh
- **blog**: http://hanks.xyz

welcome to commit [issue](https://github.com/hanks-zyh/HTextView/issues) & [pr](https://github.com/hanks-zyh/HTextView/pulls)

---
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
