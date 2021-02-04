import React from 'react';
import SockJsClient from 'react-stomp';
class quote {
    constructor(activity,accessibility,type,participants,price,link,key){
        this.activity =activity;
        this.accessibility=accessibility;
        this.type =type;
        this.participants =participants;
        this.price =price;
        this.link=link;
        this.key=key;
    }
}


class Fetched extends React.Component {
    
    constructor(props){
        super(props);
        this.state={
            book :[],
            quote : quote,
            messages : quote
        }
    }
    onMessageReceive = (msg, topic) => {
        console.log(msg);
        this.setState(({
          messages: msg.content
        }));
        console.log(msg.content);
    }
    
    sendMessage = () => {
        this.clientRef.sendMessage("/app/greetings", "hello");        
    }
    
    componentWillMount(){
        fetch("http://localhost:8080/greeting")
        .then(response => response.json())
        .then(response => this.setState({
            quote: response.content,
            book: response.id
        }));
        console.log("msg")
    }

    render(){
        
        const quote= this.state.quote.activity;
        const book= this.state.book;
        var txt=this.state.messages.activity;
        console.log(txt);
        return(
            <div>
            {book} {quote}
            <button onClick={this.sendMessage}>send</button>
            <SockJsClient url="http://localhost:8080/test" 
            topics={['/topic/greetings']} onMessage={ this.onMessageReceive }ref={ (client) => { this.clientRef = client } }/>
            {txt}
            </div>
        );
    }
}

export default Fetched;
