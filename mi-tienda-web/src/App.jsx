import { useState, useEffect } from "react";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [productos, setProductos] = useState([]);

  // Estados para Login
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // Estados para Crear Producto
  const [nombre, setNombre] = useState("");
  const [precio, setPrecio] = useState("");
  const [descripcion, setDescripcion] = useState("");

  // 1. LOGIN
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem("token", data.token);
        setToken(data.token);
      } else {
        alert("Credenciales incorrectas");
      }
    } catch (error) { console.error(error); }
  };

  // 2. LISTAR (GET)
  const fetchProductos = async () => {
    const response = await fetch("http://localhost:8080/api/v1/productos", {
      headers: { "Authorization": `Bearer ${token}` } // <--- Token aquí
    });
    if (response.ok) setProductos(await response.json());
  };

  // 3. CREAR (POST) - ¡NUEVO!
  const handleCrear = async (e) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/api/v1/productos", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`, // <--- Token aquí también
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ nombre, precio: parseFloat(precio), descripcion })
    });

    if (response.ok) {
      alert("Producto creado!");
      setNombre(""); setPrecio(""); setDescripcion(""); // Limpiar formulario
      fetchProductos(); // Recargar la lista
    }
  };

  // 4. ELIMINAR (DELETE) - ¡NUEVO!
  const handleEliminar = async (id) => {
    if(!window.confirm("¿Seguro que quieres borrar este producto?")) return;

    const response = await fetch(`http://localhost:8080/api/v1/productos/${id}`, {
      method: "DELETE",
      headers: { "Authorization": `Bearer ${token}` } // <--- Token aquí también
    });

    if (response.ok) {
      fetchProductos(); // Recargar la lista para que desaparezca
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken("");
    setProductos([]);
  };

  useEffect(() => { if (token) fetchProductos(); }, [token]);

  // --- VISTA ---
  return (
    <div style={{ padding: "20px", fontFamily: "sans-serif", maxWidth: "800px", margin: "0 auto" }}>
      <h1>Gestión de Productos</h1>

      {!token ? (
        <form onSubmit={handleLogin} style={{ border: "1px solid #ccc", padding: "20px" }}>
          <h2>Iniciar Sesión</h2>
          <input type="text" placeholder="Usuario" onChange={e => setUsername(e.target.value)} style={{display:"block", marginBottom:10}} />
          <input type="password" placeholder="Contraseña" onChange={e => setPassword(e.target.value)} style={{display:"block", marginBottom:10}} />
          <button type="submit">Entrar</button>
        </form>
      ) : (
        <div>
          <button onClick={logout} style={{ float: "right", background: "#f44336", color: "white" }}>Salir</button>

          {/* FORMULARIO DE CREAR */}
          <div style={{ background: "#f0f0f0", padding: "15px", marginBottom: "20px", borderRadius: "5px" }}>
            <h3>Agregar Nuevo Producto</h3>
            <form onSubmit={handleCrear} style={{ display: "flex", gap: "10px" }}>
              <input placeholder="Nombre" value={nombre} onChange={e => setNombre(e.target.value)} required />
              <input placeholder="Precio" type="number" value={precio} onChange={e => setPrecio(e.target.value)} required />
              <input placeholder="Descripción" value={descripcion} onChange={e => setDescripcion(e.target.value)} required />
              <button type="submit" style={{ background: "#4CAF50", color: "white" }}>Guardar</button>
            </form>
          </div>

          {/* LISTA */}
          <h3>Inventario</h3>
          <ul style={{ listStyle: "none", padding: 0 }}>
            {productos.map((p) => (
              <li key={p.id} style={{ borderBottom: "1px solid #ddd", padding: "10px", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <span>
                  <strong>{p.nombre}</strong> (${p.precio}) - <small>{p.descripcion}</small>
                </span>
                <button
                  onClick={() => handleEliminar(p.id)}
                  style={{ background: "#ff0000", color: "white", border: "none", padding: "5px 10px", cursor: "pointer", borderRadius: "3px" }}
                >
                  X
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default App;