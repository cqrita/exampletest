import React from 'react';
import SockJsClient from 'react-stomp';
class Message {
    constructor(roomId, content, id) {
        if (arguments.length === 3) {
            this.id = id;
            this.roomId = roomId;
            this.content = content;
        } else {
            this.roomId = roomId;
            this.content = content;
        }
    }
}


function Messages({ messages }) {
    return (
        <div>
            {messages.content}
        </div>
    );
}

class Chatting extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            messages: [],
            value: ''
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
    }
    componentDidUpdate() {
        this.scrollToBottom();
    }
    getMessages(id, ob) {
        fetch("http://localhost:8080/messageList?roomId=" + id)
            .then(response => response.json())
            .then(response => ob.setState({
                messages: response
            }));
    }
    sendMessage = (text, roomId) => {
        const message = new Message(roomId, text)
        this.clientRef.sendMessage("/app/sendMessage", JSON.stringify(message));
    }
    onMessageReceive = (msg, topic) => {
        this.setState(prevState => ({
            messages: [...prevState.messages, msg]
        }));
    }
    handleChange(event) {
        this.setState({ value: event.target.value });
    }
    handleSubmit(event) {
        this.sendMessage(this.state.value, this.state.chatroom.id)
        event.preventDefault();
    }
    scrollToBottom = () => {
        this.messagesEnd.scrollIntoView({ behavior: "smooth" });
    }
    render() {
        const messages = this.state.messages;
        return (
            <div>
                <SockJsClient url="http://localhost:8080/test"
                    topics={['/topic/getMessage']} onMessage={this.onMessageReceive} ref={(client) => { this.clientRef = client }} />
                <div style={{ overflowY: 'scroll', height: '100px' }}>
                    {messages.map(messages => (
                        <Messages messages={messages} key={messages.id} />
                    ))}
                    <div style={{ float: "left", clear: "both" }}
                        ref={(el) => { this.messagesEnd = el; }}>
                    </div>
                </div>
                <form onSubmit={this.handleSubmit}>
                    <label>
                        <input type="text" value={this.state.value} onChange={this.handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        )
    }
}

export default Chatting;
