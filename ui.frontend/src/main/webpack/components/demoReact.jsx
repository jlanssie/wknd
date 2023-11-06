import React from "react";

export const DemoReact = (props) => {
    return <h1>Hello {props.name || "world"}</h1>
}