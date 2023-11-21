import React from "react";

export const Demo = (props) => {
    return <h1>Hello {props.name || "World"}!</h1>
}