var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

import React from 'react';
import SockJsClient from 'react-stomp';

var Message = function Message(roomId, content, id) {
    _classCallCheck(this, Message);

    if (arguments.length === 3) {
        this.id = id;
        this.roomId = roomId;
        this.content = content;
    } else {
        this.roomId = roomId;
        this.content = content;
    }
};

function Messages(_ref) {
    var messages = _ref.messages;

    return React.createElement(
        'div',
        null,
        messages.content
    );
}

var Chatting = function (_React$Component) {
    _inherits(Chatting, _React$Component);

    function Chatting(props) {
        _classCallCheck(this, Chatting);

        var _this = _possibleConstructorReturn(this, (Chatting.__proto__ || Object.getPrototypeOf(Chatting)).call(this, props));

        _this.sendMessage = function (text, roomId) {
            var message = new Message(roomId, text);
            _this.clientRef.sendMessage("/app/sendMessage", JSON.stringify(message));
        };

        _this.onMessageReceive = function (msg, topic) {
            _this.setState(function (prevState) {
                return {
                    messages: [].concat(_toConsumableArray(prevState.messages), [msg])
                };
            });
        };

        _this.scrollToBottom = function () {
            _this.messagesEnd.scrollIntoView({ behavior: "smooth" });
        };

        _this.state = {
            messages: [],
            value: ''
        };
        _this.handleChange = _this.handleChange.bind(_this);
        _this.handleSubmit = _this.handleSubmit.bind(_this);
        _this.scrollToBottom = _this.scrollToBottom.bind(_this);
        return _this;
    }

    _createClass(Chatting, [{
        key: 'componentDidUpdate',
        value: function componentDidUpdate() {
            this.scrollToBottom();
        }
    }, {
        key: 'getMessages',
        value: function getMessages(id, ob) {
            fetch("http://localhost:8080/messageList?roomId=" + id).then(function (response) {
                return response.json();
            }).then(function (response) {
                return ob.setState({
                    messages: response
                });
            });
        }
    }, {
        key: 'handleChange',
        value: function handleChange(event) {
            this.setState({ value: event.target.value });
        }
    }, {
        key: 'handleSubmit',
        value: function handleSubmit(event) {
            this.sendMessage(this.state.value, this.state.chatroom.id);
            event.preventDefault();
        }
    }, {
        key: 'render',
        value: function render() {
            var _this2 = this;

            var messages = this.state.messages;
            return React.createElement(
                'div',
                null,
                React.createElement(SockJsClient, { url: 'http://localhost:8080/test',
                    topics: ['/topic/getMessage'], onMessage: this.onMessageReceive, ref: function ref(client) {
                        _this2.clientRef = client;
                    } }),
                React.createElement(
                    'div',
                    { style: { overflowY: 'scroll', height: '100px' } },
                    messages.map(function (messages) {
                        return React.createElement(Messages, { messages: messages, key: messages.id });
                    }),
                    React.createElement('div', { style: { float: "left", clear: "both" },
                        ref: function ref(el) {
                            _this2.messagesEnd = el;
                        } })
                ),
                React.createElement(
                    'form',
                    { onSubmit: this.handleSubmit },
                    React.createElement(
                        'label',
                        null,
                        React.createElement('input', { type: 'text', value: this.state.value, onChange: this.handleChange })
                    ),
                    React.createElement('input', { type: 'submit', value: 'Submit' })
                )
            );
        }
    }]);

    return Chatting;
}(React.Component);

export default Chatting;