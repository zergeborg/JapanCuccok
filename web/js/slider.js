(function($){
    $.fn.extend({
        transparentSlider: function(options) {

            var mySelf = this;

            var defaults =
            {
                elId                 : undefined,
                ulObject             : undefined,
                params               : [],
                nextTitle            : undefined,
                direction            : 'right', // Which direction?
                lastDirection        : 'right', // The direction in the previous round
                lastCursorX          : 0, // cursor x-position at most recent mouse event
                lastCursorY          : 0, // cursor y-position at most recent mouse event
                width                : 800,
                height               : 600,
                titleOpacity         : 1, // opacity of title and navigation
                titleSpeed           : 1000, // speed of title display
                titleHeight          : 90, // height of the titles
                animationFrequency   : 6000,
                animationDuraion     : 1500,
                left                 : 0,
                lastLeft             : 0,
                initialState         : 'Inactive',
                currentState         : 'Inactive',
                animateProp          : undefined,
                nextImage            : undefined,
                prevImage            : undefined,
                placeHolder          : undefined,
                currentTicker        : undefined // returned by setInterval, if a ticker is currently running
            };
            var options = $.extend(defaults, options);

            return this.each(function() {

                var mySelf = this;
                var o=options;
                o.elId = $(mySelf).attr('id');
                o.params[o.elId] = $.extend({}, o);
                o.width = o.params[o.elId].width;
                o.height = o.params[o.elId].height;

                setDirectionRight : function setDirectionRight() {
                    o.lastDirection = o.direction;
                    o.direction = 'right';
                }

                setDirectionLeft : function setDirectionLeft() {
                    o.lastDirection = o.direction;
                    o.direction = 'left';
                }

                initPlaceHolder : function initPlaceHolder() {
                    o.placeHolder = $("<li class='ts-li' id='placeholder'><a><span class='imageWrapper'><div/></span></a></li>");
                    o.placeHolder.css({'width':o.params[o.elId].width});
                }

                createTitleBar: function createTitleBar(i) {
                    // create title bar
                    $('#labelWrapper').append("<div class='ts-title' id='ts-title-" + o.elId + i +"'></div>");
                    var $title = $('#ts-title-' + o.elId + i);
                    return $title;
                }

                initTitleBar: function initTitleBar($title, $titleContent) {
                    $title.css({'height' : o.params[o.elId].titleHeight});
                    $title.html($titleContent);
                    return o;
                }

                initImages : function initImages() {
                    var imgList = $('.ts-slideshow span div', '#slider');
                    for(var i = 0, max = imgList.length; i < max; i += 1) {
                        var $imageWrapper = $(imgList[i]).parent();
                        var $imageWrapperNext = $imageWrapper.next();
                        var $title = createTitleBar(i);
                        var $titleContent = $imageWrapperNext.is('span') ? $imageWrapperNext.html() : '';
                        initTitleBar($title, $titleContent);
                        var $anchor = $imageWrapper.parent();
                        var $ts_li = $anchor.parent();
                        $ts_li.css({'width':o.width});
                        $imageWrapperNext.hide();
                    }
                    return o;
                }

                //Event listener means the element which is used as the target to attach the content to
                initEventListener : function initEventListener() {
                    var imageWidth = o.width+40; //40px is the background
                    var imageHeight = o.height; //80px is the background
                    // set panel
                    $('#' + o.elId).css({
                        'margin'  :'auto',
                        'clear'   :'both',
                        'overflow':'hidden',
                        'width'   :imageWidth,
                        'height'  :imageHeight,
                        'background-size': imageWidth+'px '+imageHeight+'px',
                        'position':'relative'
                    }).wrap("<div class='transparent-slider' id='transparent-slider-" + o.elId + "' />");

                    // add slideshow to the DOM tree
                    $('.ts-slideshow', '#' + o.elId).attr('id', 'ts-slideshow-' + o.elId);
                    o.ulObject = $('#ts-slideshow-' + o.elId);
                    return o;
                }

                initSlideshowPanel : function initSlideshowPanel() {
                    var imageWidth = o.width;
                    var imageHeight = o.height;
                    // slideshow width should be the width of [image_width]*[nr of images]
                    o.ulObject.css(
                        {
                            'width'  :(imageWidth * 3) + (20 * 3), // 20 equals to the left and right margins between images
                            'height' :imageHeight-30 //30 pixels are needed to make sure the image fits in its container
                        }
                    );
                    return o;
                }

                setLeft : function setLeft(marginLeft) {
                    o.ulObject.css(
                        {
                            left: marginLeft
                        }
                    );
                    return o;
                }

                initPrev : function initPrev() {
                    //$('#prev').appendTo($('#transparent-slider-' + o.elId).children('#slider'));
                    $('#prev').css({'opacity':'0', 'z-index':'9999'});
                }

                initNext : function initNext() {
                    //$('#next').appendTo($('#transparent-slider-' + o.elId).children('#slider'));
                    $('#next').css({'opacity':'0', 'z-index':'9999'});
                }

                displaySlideshow : function displaySlideshow() {
                    // Display slideshow
                    o.ulObject.show();
                    // Display images
                    $('#' + o.elId + ' .ts-li').show();
                    // Display title
                    $(o.nextTitle).show();
                }

                selectNextImage : function selectNextImage() {
                    if(o.direction === 'left') {
                        o.nextImage = $('li:first', o.ulObject);
                        o.firstImage = o.nextImage.next();
                        o.firstImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        o.secondImage = o.nextImage.next().next();
                        o.secondImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        o.thirdImage = o.nextImage.next().next().next();
                        o.thirdImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        var currentId = o.nextImage.attr("id");
                        o.nextTitle = '#ts-title-' + currentId;
                    } else {
                        o.firstImage = $('li:first', o.ulObject);
                        o.firstImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        o.secondImage = $('li:first', o.ulObject).next();
                        o.secondImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        o.nextImage = $('li:last', o.ulObject);
                        o.nextImage.css({
                            'display' : 'inline',
                            'float'   : 'left'
                        });
                        var currentId = o.nextImage.attr("id");
                        o.nextTitle = '#ts-title-' + currentId;
                    }
                }

                detachNextImage : function detachNextImage() {
                    o.nextImage.remove();
                }

                attachPlaceHolder : function attachPlaceHolder() {
                    if(o.direction === 'left') {
                        $('li:last', o.ulObject).after(o.placeHolder);
                    } else {
                        $('li:first', o.ulObject).before(o.placeHolder);
                    }
                }

                replacePlaceHolder : function replacePlaceHolder() {
                    // This is needed because jQuery removes the elements after replaceWith called
                    var tempImageObject = o.nextImage.clone();
                    $('#placeholder').replaceWith(tempImageObject);
                }

                clearCursorPosition : function clearCursorPosition() {
                    o.lastCursorX = -1;
                    o.lastCursorY = -1;
                }

                initLeft : function initLeft() {
                    var width = o.width;
                    // get the target position for the slideshow div
                    if(o.lastDirection === o.direction) {
                        if(o.direction === 'left') {
                            setLeft(-o.width-20);
                            o.left = (-o.width*2)-20;
                        } else {
                            setLeft((-o.width*2)-30);
                            o.left = -o.width-30;
                        }
                    } else {
                        o.left = o.lastLeft;
                        o.lastDirection = o.direction;
                    }
                    o.lastLeft = o.left;
                    return o;
                }

                initAnimateProps : function initAnimateProps() {
                    // jQuery.animate() will use this one!
                    o.animateProp = {
                        left : o.left
                    };
                    return o;
                }

                animate : function animate() {
                    var titleCB = animateTitle;
                    var buttonCB = animateButtons;
                    o.ulObject.css({ 'position':'absolute' }).stop().animate(
                        o.animateProp,
                        {
                            easing: 'easeOutBack',
                            duration: o.animationDuraion,
                            complete: function() {
                                var callbackParam = o;
                                var titleCallback = titleCB;
                                var buttonCallback = buttonCB;
                                callbackParam.ulObject.css({ 'position':'relative' });
                                titleCallback(callbackParam);
                                buttonCallback(callbackParam);
                            }
                        }
                    );
                }

                animateTitle : function animateTitle(o) {
                    $(o.nextTitle).css({ 'position':'absolute' }).show().animate(
                        {},
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $(o.nextTitle).css({ 'position':'relative' });
                            }
                        }
                    );
                }

                animateButtons : function animateButtons(o) {
                    $('#next').css({ 'position':'absolute', 'opacity':0 }).animate(
                        {
                            'opacity':o.titleOpacity
                        },
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $('#next').css({ 'opacity':1 });
                            }
                        }
                    );
                    $('#prev').css({ 'position':'absolute', 'opacity':0 }).animate(
                        {
                            'opacity':o.titleOpacity
                        },
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $('#prev').css({ 'opacity':1 });
                            }
                        }
                    );
                }

                hideTitle : function hideTitle() {
                    $('.ts-title').hide();
                }

                doActionTransition : function doActionTransition(anotherState, anotherEventType, event) {
                    return actionTransitionFunctions[anotherState][anotherEventType].call($(mySelf), event);
                }

                startTicker : function startTicker() {
                    o.currentTicker = setInterval(function() {
                        // Slide!
                        doActionTransition('Inactive','timetick',undefined);
                    }, o.animationFrequency);

                    return o;
                }

                clearTicker : function clearTicker() {
                    var ticker = o.currentTicker;
                    if (ticker) clearInterval(ticker);
                    o.currentTicker = null;
                    return o;
                }

                handleEvent: function handleEvent(event) {
                    var actionTransitionFunction = actionTransitionFunctions[o.currentState][event.type];
                    if (!actionTransitionFunction) actionTransitionFunction = unexpectedEvent;
                    var nextState = actionTransitionFunction.call(mySelf, event);
                    if (!nextState) nextState = o.currentState;
                    if (!actionTransitionFunctions[nextState]) nextState = undefinedState(event, nextState);
                    o.currentState = nextState;
                }

                unexpectedEvent: function unexpectedEvent(event) {
                    clearTicker();
                    alert("TransparentSlider handled unexpected event '" + event.type + "' in state '" + o.currentState + "' for id='" + o.elId + "' running browser " + window.navigator.userAgent);
                    return o.initialState;
                }

                undefinedState: function undefinedState(event, state) {
                    clearTicker();
                    alert("TransparentSlider transitioned to undefined state '" + state + "' from state '" + o.currentState + "' due to event '" + event.type + "' from HTML element id='" + o.elId + "' running browser " + window.navigator.userAgent);
                    return o.initialState;
                }

                var actionTransitionFunctions = {

                    Inactive: {
                        init: function init(event) {
                            initImages();
                            initEventListener();
                            initSlideshowPanel();
                            initPlaceHolder();
                            displaySlideshow();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        run: function run(event) {
                            if(o.lastDirection === o.direction) {
                                selectNextImage();
                            }
                            initPrev();
                            initNext();
                            return doActionTransition('Inited', 'move', event);
                        },

                        timetick: function timetick(event) {
                            return doActionTransition('Inactive', 'run', event);
                        },

                        onnext: function onnext(event) {
                            setDirectionRight();
                            clearTicker();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        onprev: function onprev(event) {
                            setDirectionLeft();
                            clearTicker();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        click: function click(event) {
                            var $eventTarget = $(event.target);
                            if($eventTarget.parent().attr('id') === 'next' || $eventTarget.attr('id') === 'next') {
                                return doActionTransition('Inactive', 'onnext', event);
                            } else {
                                return doActionTransition('Inactive', 'onprev', event);
                            }
                        }

                    },

                    Inited: {
                        move: function move(event) {
                            if(o.lastDirection === o.direction) {
                                attachPlaceHolder();
                                replacePlaceHolder();
                            }
                            return doActionTransition('Move', 'slide', event);
                        },

                        onnext: function onnext(event) {
                            return doActionTransition('Inactive', 'onnext', event);
                        },

                        onprev: function onprev(event) {
                            return doActionTransition('Inactive', 'onprev', event);
                        },

                        click: function click(event) {
                            var $eventTarget = $(event.target);
                            if($eventTarget.attr('id') === 'next') {
                                return doActionTransition('Inited', 'onnext', event);
                            } else {
                                return doActionTransition('Inited', 'onPrev', event);
                            }
                        }

                    },

                    Move: {
                        slide: function slide(event) {
                            initLeft();
                            initAnimateProps();
                            animate();
                            return doActionTransition('Finish', 'reset', event);
                        },

                        onnext: function onnext(event) {
                            setDirectionRight();
                            return doActionTransition('Interrupt', 'slide', event);
                        },

                        onprev: function onprev(event) {
                            setDirectionLeft();
                            return doActionTransition('Interrupt', 'slide', event);
                        },

                        click: function click(event) {
                            var $eventTarget = $(event.target);
                            if($eventTarget.attr('id') === 'next') {
                                return doActionTransition('Move', 'onnext', event);
                            } else {
                                return doActionTransition('Move', 'onPrev', event);
                            }
                        }

                    },

                    Interrupt: {
                        slide: function slide() {
                            return doActionTransition('Move', 'slide', event);
                        }
                    },

                    Finish: {
                        reset: function reset(event) {
                            hideTitle();
                            detachNextImage();
                            clearCursorPosition();
                            clearTicker();
                            startTicker();
                            return "Inactive";
                        }
                    }

                };

                (function () {
                    $('#next').click(function (event) {
                        handleEvent(event);
                    });
                })();

                (function () {
                    $('#prev').click(function (event) {
                        handleEvent(event);
                    });
                })();

                doActionTransition('Inactive','init',undefined);
            })
        }
    });
})(jQuery);