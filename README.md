# ResizableCssLayout Add-on for Vaadin 8

ResizableCssLayout is an UI component add-on for Vaadin 8.


## Online demo

Try the add-on demo at http://pekka.app.fi/resizablecsslayout-demo/ (vaadin 7 demo)

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/resizablecsslayout

## Building and running demo
cd resizablecsslayout
mvn clean install
cd ../demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

 
## Release notes
### Version 1.2.0
- compiled for working for Vaadin Framework 8.0.5 
- fixed an issue where you could never resize to the container parents edges
- fixed an issue where resizing would start highlighting page elements once you drag outside the the parent boundaries.
KWOWN ISSUE : something is terribly wrong with the autoacceptresize functionality, I'm not sure what it is even for. 
 In the meantime use,  .setAutoAcceptResize(false); 

### Version 1.1.0
- Support keeping aspect ratio when resizing <a href="https://github.com/pleku/resizablecsslayout/issues/1">#1</a>
- Fixed 1px offset error when resizing <a href="https://github.com/pleku/resizablecsslayout/issues/3">#3</a>
- Pressing escape should cancel the resize <a href="https://github.com/pleku/resizablecsslayout/issues/4">#4</a>
- Update the right/bottom coordinates when inside a AbsoluteLayout <a href="https://github.com/pleku/resizablecsslayout/issues/5">#5</a>
- Fixed resize handles being partly hidden behind a full-size Grid component <a href="https://github.com/pleku/resizablecsslayout/issues/8">#8</a>

### Version 1.0.1
- Fixed invalid javadocs
	
### Version 1.0.0
- toggle resize mode on/off
- listener and events for resize start and resize end
- automatically accept resize on client side with autoAcceptResize
- allows canceling resize on server side with ResizeEndEvent & cancelResize()
- set resize location size in pixels

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the following features are planned for upcoming releases:
- enabled/disable ResizeLocations (eg. only corners)

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

ResizableCssLayout is written by Pekka Hyvönen pekka@vaadin.com

# Developer Guide

## Getting started

For example, see src/test/java/org/vaadin/template/demo/DemoUI.java
