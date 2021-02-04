import React from 'react';
import SockJsClient from 'react-stomp';
class Message{
    constructor(rid,content,id){
        if(arguments.length===3){
            this.id=id;
            this.rid=rid;
            this.content=content;
        }else{
            this.rid=rid;
            this.content=content;
        }
    }
}

class Room{
    constructor(id){
        this.id=id;
    }
}

function Messages({messages}){
    return(
        <div>
            {messages.content}
        </div>
    );
}

function Rooms({room,ob}) {
    return(
        <button onClick={() =>ob.getRoom(room.id,ob)}>{room.id}</button>
    )
}

class Chatting extends React.Component{
    constructor(props){
        super(props)
        this.state={
            roomList: [],
            messages: [],
            room: Room,
            value: ''
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.createroom = this.createroom.bind(this);
        this.deleteroom = this.deleteroom.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
        this.getRoomList =this.getRoomList.bind(this);
    }
    componentDidMount(){
        this.getRoomList();
        this.scrollToBottom();
    }
    getRoomList(){
        fetch("http://localhost:8080/roomList")
            .then(response => response.json())
            .then(response => this.setState({
                roomList: response
        }));
    }
    componentDidUpdate() {
        this.scrollToBottom();
    }      
    getMessages(id,ob){
        fetch("http://localhost:8080/messageList?roomId="+id)
            .then(response => response.json())
            .then(response => ob.setState({
                messages: response
        }));
    }
    getRoom(id,ob){
        fetch("http://localhost:8080/room?id="+id)
            .then(response => response.json())  
            .then(response =>
                ob.setState({
                    room: response,
                }))
            .then(ob.getMessages(id,ob))
            .catch((error) => {
                ob.setState({
                    room: Room,
                })
            });
        this.getRoomList();
    }
    createroom(){
        fetch("http://localhost:8080/createRoom")
            .then(response => response.json())
            .then(response => this.setState({
                roomList: response
        }));
    }
    deleteroom(id){
        fetch("http://localhost:8080/deleteRoom?id="+id)
            .then(response => response.json())
            .then(response => this.setState({
                room: Room,
                roomList: response
        }));
    }
    sendMessage = (text,rid) => {
        const message=new Message(rid,text)
        this.clientRef.sendMessage("/app/sendMessage",JSON.stringify(message));        
    }
    onMessageReceive= (msg, topic) =>{
        if(this.state.room.id===msg.rid){
            this.setState(prevState => ({
                messages: [...prevState.messages, msg]
            }));
        }
    }
    handleChange(event) {
        this.setState({value: event.target.value});
    }
    
    handleSubmit(event) {
        this.sendMessage(this.state.value,this.state.room.id)
        this.setState({value: ''});
        event.preventDefault();
    }
    scrollToBottom = () => {
        this.messagesEnd.scrollIntoView({ behavior: "smooth" });
    }
      
    render(){
        const rid=this.state.room.id;
        const roomList=this.state.roomList;
        if(rid==null){
            return(
                <div>
                    {roomList.map(room => (
                        <Rooms room={room} ob={this} key={room.id} />
                    ))}
                    <div>
                        <button onClick={this.createroom}>create</button>
                    </div>
                    <div style={{ float:"left", clear: "both" }}
                            ref={(el) => { this.messagesEnd = el; }}>
                    </div>
                </div>   
            )
        }else{
            const messages=this.state.messages;
            return(
                <div>
                    <p>{rid}</p>
                    {roomList.map(room => (
                        <Rooms room={room} ob={this} key={room.id} />
                    ))}
                    <div>
                        <button onClick={this.createroom}>create</button>
                    </div>
                    <button onClick={() =>this.deleteroom(rid)}>delete</button>
                    
                    <SockJsClient url="http://localhost:8080/test" 
                    topics={['/topic/getMessage']} onMessage={ this.onMessageReceive }ref={ (client) => { this.clientRef = client } }/>
                    <div style={{ overflowY: 'scroll', height: '100px' }}>
                        {messages.map(messages => (
                            <Messages messages={messages} key={messages.id} />
                        ))}
                        <div style={{ float:"left", clear: "both" }}
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
}

export default Chatting;
