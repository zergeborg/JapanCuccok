jQuery.fx.interval = 30;
(function($){

    Function.prototype.bind = function(scope) {
        var _function = this;

        return function() {
            return _function.apply(scope, arguments);
        }
    }

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
                width                : 900,
                height               : 675,
                thumbnailWidth       : '200px',
                thumbnailHeight      : '150px',
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
                currentTicker        : undefined // returned by setInterval, if a ticker is currently running
            };
            var options = $.extend(defaults, options);

            return this.each(function() {

                var mySelf = this;
                var randID = Math.round(Math.random()*100000000);
                var curr = (options.direction === 'right')?1:-1;
                var dist = 0;
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

                createTitleBar: function createTitleBar(i) {
                    // create title bar
                    $('#labelWrapper').append("<div class='ts-title' id='ts-title-" + o.elId + i +"'></div>");
                    var $title = $('#ts-title-' + o.elId + i);
                    return $title;
                }

                initTitleBar: function initTitleBar($title, $titleContent) {
                    //$title.css({'height' : o.params[o.elId].titleHeight});
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
                        'position':'relative',
                        'padding-top':'30px',
                        'left'    :'-10px'
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

                initThumbnails : function initThumbnails()
                {
                    var object = $("#sliderAndLabel");
                    var numImages = $('li', object).length;
                    var imageWidth = o.width;
                    var thumb,i;
                    // Build thumbnail viewer and thumbnail divs
                    object.after('<div class="thumbs" id="thumbs'+randID+'"></div>');
                    $('#thumbs'+randID).width(imageWidth);
                    for(i=0;i<numImages;i++)
                    {
                        thumb = $('.ts-li .imageWrapper:eq('+(i)+')', object).css('background-image');
                        $('#thumbs'+randID).append('<div class="thumb" id="thumb'+randID+'_'+(i)+'" style="background-image:'+thumb+';background-size:'+o.thumbnailWidth+';width:'+o.thumbnailWidth+';height:'+o.thumbnailHeight+';line-height:'+o.thumbnailHeight+';">'+(i)+'</div>');
                        if(i===0) $('#thumb'+randID+'_1').css({'border-color':'#ff0000'});
                    }
                    // Next two lines are a special case to handle the first list element which was originally the last
                    thumb = $('.ts-li .imageWrapper:first', object).css('background-image');
                    $('#thumb'+randID+'_'+numImages).css({'background-image':thumb});

                    // Assign click handler for the thumbnails. Normally the format $('.thumb') would work but since it's outside of our object (obj) it would get called multiple times
                    $('#thumbs'+randID+' div').bind('click', handleEvent); // We use bind instead of just plain click so that we can repeatedly remove and reattach the handler
                }

                thumbClick : function thumbClick(event)
                {
                    var $eventTarget = $(event.target);
                    var id = $eventTarget.attr('id');
                    var target_num = id.split('_')[1];
                    if(curr != target_num)
                    {
                        $("[id*='thumb"+randID+"']").css({'border-color':'rgba(0, 0, 0, 0.3)'});
                        $('#thumb'+randID+'_'+target_num).css({'border-color':'#ccc'});
                    }
                    if(target_num > curr)
                    {
                        dist = target_num - curr;
                        setDirectionLeft();
                    }
                    if(target_num < curr)
                    {
                        dist = curr - target_num;
                        setDirectionRight();
                    }
                }

                setLeft : function setLeft(leftPosition) {
                    o.ulObject.css(
                        {
                            left: leftPosition
                        }
                    );
                    return o;
                }

                displaySlideshow : function displaySlideshow() {
                    // Display slideshow
                    o.ulObject.show();
                    // Display images
                    $('#' + o.elId + ' .ts-li').show();
                    // Display title
                    $(o.nextTitle).show();
                }

                detachNextImage : function detachNextImage() {
                    o.nextImage.remove();
                }

                clearCursorPosition : function clearCursorPosition() {
                    o.lastCursorX = -1;
                    o.lastCursorY = -1;
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

                multiStep : function multiStep(steps, args, callback) {

                    var tasks = steps.concat(); //clone the array

                    setTimeout(function(){

                        //execute the next task
                        var task = tasks.shift();
                        task.apply(null, args || []);

                        //determine if there is more
                        if(tasks.length > 0) {
                            setTimeout(arguments.callee, 25);
                        } else {
                            callback();
                        }
                    }, 25);
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
                            initThumbnails();
                            displaySlideshow();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        run: function run(event) {
                            // selectNextImage()
                            if(o.lastDirection === o.direction) {
                                if(o.direction === 'left') {
                                    o.nextImage = $('li:first', o.ulObject);
                                    o.firstImage = o.nextImage.next();
                                    o.firstImage.css({
                                        'display' : 'inline',
                                        'float'   : 'left'
                                    });
                                    o.secondImage = o.firstImage.next();
                                    o.secondImage.css({
                                        'display' : 'inline',
                                        'float'   : 'left'
                                    });
                                    o.thirdImage = o.secondImage.next();
                                    o.thirdImage.css({
                                        'display' : 'inline',
                                        'float'   : 'left'
                                    });
                                    var currentId = o.thirdImage.attr("id");
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
                                    var currentId = o.firstImage.attr("id");
                                    o.nextTitle = '#ts-title-' + currentId;
                                }
                            }
                            // initCurrent()
                            var object = o.ulObject;
                            var numImages = $('li', object).length;
                            if(o.direction === 'left') {
                                if(curr === numImages-1) {
                                    curr = 0;
                                } else {
                                    curr = curr+1;
                                }
                            } else {
                                if(curr === 0) {
                                    curr = numImages-1;
                                } else {
                                    curr = curr-1;
                                }
                            }
                            // moveThumbnailBorder()
                            $("[id*='thumb"+randID+"']").css({'border-color':'rgba(0, 0, 0, 0.3)'});
                            $('#thumb'+randID+'_'+curr).css({'border-color':'#ccc'});
                            // initPrev()
                            $('#prev').css({'opacity':'0', 'z-index':'9999'});
                            // initNext()
                            $('#next').css({'opacity':'0', 'z-index':'9999'});
                            // attachNextImage()
                            if(o.lastDirection === o.direction) {
                                if(o.direction === 'left') {
                                    $('li:last', o.ulObject).after(o.nextImage.clone());
                                } else {
                                    $('li:first', o.ulObject).before(o.nextImage.clone());
                                }
                            }
                            // initLeft()
                            var width = o.width;
                            var direction = o.direction;
                            // get the target position for the slideshow div
                            if(o.lastDirection === direction) {
                                if(direction === 'left') {
                                    setLeft(-width-30);
                                    o.left = (-width*(2))-30;
                                } else {
                                    setLeft((-width*(2))-(numImages*10)-40);
                                    o.left = -width-10;
                                }
                            } else {
                                if(direction === 'left') {
                                    o.left = o.lastLeft - width-20;
                                } else {
                                    o.left = o.lastLeft + width+20;
                                }
                                o.lastDirection = direction;
                            }
                            o.lastLeft = o.left;
                            // initAnimateProps()
                            // jQuery.animate() will use this one!
                            o.animateProp = {
                                'position':'absolute',
                                left : o.left
                            };
                            // animate()
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
                            hideTitle();
                            detachNextImage();
                            clearCursorPosition();
                            clearTicker();
                            startTicker();
                            return "Inactive";
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

                        onthumb: function onthumb(event) {
                            var j;
                            thumbClick(event);
                            for(j = 0; j < dist; j++)
                            {
                                doActionTransition('Inactive', 'run', event);
                            }
                            dist = 0;
                            return "Inactive";
                        },

                        click: function click(event) {
                            var $eventTarget = $(event.target);
                            if($eventTarget.parent().attr('id') === 'next' || $eventTarget.attr('id') === 'next') {
                                return doActionTransition('Inactive', 'onnext', event);
                            } else if($eventTarget.parent().attr('class') === 'thumb' || $eventTarget.attr('class') === 'thumb') {
                                return doActionTransition('Inactive', 'onthumb', event);
                            } else {
                                return doActionTransition('Inactive', 'onprev', event);
                            }
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