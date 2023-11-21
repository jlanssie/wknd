import React from "react";

const Demo = (props) => {
    return <h1>Hello {props.name || "World"}!</h1>
}

export default Demo;