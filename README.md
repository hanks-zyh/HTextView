# HTextView
Animation effects with custom font support to TextView

![](https://img.shields.io/hexpm/l/plug.svg)
![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![](https://img.shields.io/badge/Android-CustomView-blue.svg)

see [iOS Effects](https://github.com/lexrus/LTMorphingLabel)

---

## Screenshot

<div  style="text-align:center;">
  <img src="https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo.gif" width="400px"/> 
</div>

| type  | gif |
| :-- | :-- |
| [Scale(Default)](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/ScaleText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo3.gif) |
| [Fall](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/FallText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo6.gif) |
| [EvaporateText](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/EvaporateText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo5.gif) |
| [Line](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/LineText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo7.gif) |
| [Sparkle](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/SparkleText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo8.gif) |
| [Typer](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/TyperText.java) |  ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/typer.gif) |
| [Rainbow](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/RainBowText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/rainbow.gif) |
| [Anvil](https://github.com/hanks-zyh/HTextView/blob/master/htextview-library/src/main/java/com/hanks/htextview/animatetext/AnvilText.java) | ![](https://github.com/hanks-zyh/HTextView/blob/master/screenshot/demo2.gif) |


## Usage

### Add library dependency to project:

```groovy
compile 'hanks.xyz:htextview-library:0.1.3'
```

### Add HTextView to your layout:

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
       htext:fontAsset="fonts/font-name.ttf" />
```

- `animateType` can be `scale`, `evaporate`, `fall`, `sparkle`, `anvil`, `line`, `pixelate`, `typer`, or `rainbow`.
- `fontAsset` is optional. It indicates what is the path of true type font to be used. If not set, the default TextView font will be used.

Don't forget to set the layout namespace to res-auto:

```xml
xmlns:htext="http://schemas.android.com/apk/res-auto"
```

### Customize HTextView in runtime:

```java
hTextView = (HTextView) findViewById(R.id.text);
hTextView.setTypeface(FontManager.getInstance(getAssets()).getFont("fonts/font-name.ttf")); 
// be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
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

## Thanks

Thank [Skykai521](https://github.com/Skykai521) for writing an article about HTextView:

:point_right:[HTextView源代码分析](http://skykai521.github.io/2016/01/30/HTextView%E6%BA%90%E4%BB%A3%E7%A0%81%E5%88%86%E6%9E%90/)

 
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
