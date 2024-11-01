import React, {useState} from "react";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [jwt, setJwt] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:9000/api/v1/auth/login",{
                method:"POST",
                headers: {
                    "Content-Type": "application/json", 
                },
                body: JSON.stringify({email, password}),
            }
            );

            if (response.ok){
                const data = await response.json();
                console.log(data);
                setJwt(data.token)
                setMessage("Login Successful");
            } else {
                setMessage("Username and/or password is incorrect")
            }

        } catch (error) {
            console.log("Oops! An error has occurred: " + error)
            setMessage("Something went wrong. Please try again")
        }
    }
    
    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleLogin}>
                <div>
                    <label>Username: </label>
                    <input type="text" value={email} onChange={(e) => setEmail(e.target.value)}/><br/><br/>
                    <label>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/><br/><br/>
                </div>
                <button type="submit">Login</button>
            </form>
            {message && <p>{message}</p>}
            {jwt && <p>{jwt}</p>}
        </div>
    )
}

export default Login;