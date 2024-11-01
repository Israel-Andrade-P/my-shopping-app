import React, {useState} from "react";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [jwt, setJwt] = useState("");
    const [id, setId] = useState("");
    const [profile, setProfile] = useState(null);

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
                setId(data.userId)
                getUserProfile(data.token, data.userId)
            } else {
                setMessage("Username and/or password is incorrect")
            }

        } catch (error) {
            console.log("Oops! An error has occurred: " + error)
            setMessage("Something went wrong. Please try again")
        }
    }

    const getUserProfile = async (token, userId) => {
        try {
            const response = await fetch("http://localhost:9000/api/v1/users/user/" + userId,{
                method:"GET",
                headers: {
                    "Authorization": `Bearer ${token}`, 
                },
            }
            );

            if (response.ok){
                const data = await response.json();
                console.log(data);
                setProfile(data);
            } else {
                setMessage("User profile unavailable.")
            }

        } catch (error) {
            console.log("Oops! An error has occurred: " + error)
            setMessage("Something went wrong. Please try again")
        }
    }
    
    return (
        <div>

            {!profile ? (
            <form onSubmit={handleLogin}>
                <div>
                    <label>Username: </label>
                    <input type="text" value={email} onChange={(e) => setEmail(e.target.value)}/><br/><br/>
                    <label>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/><br/><br/>
                </div>
                <button type="submit">Login</button>
            </form>
            ) : (
                <div>
                    <h3>User Profile</h3>
                    <p>User ID: {profile.userId}</p>
                    <p>Name: {profile.name}</p>
                    <p>Email: {profile.email}</p>
                    <p>Authorities: {profile.authorities}</p>
                </div>    
            )}
             {message && <p>{message}</p>}
             {jwt && <p>{jwt}</p>}
        </div>
    )
}

export default Login;