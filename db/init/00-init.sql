-- Crea DBs para ambos servicios
CREATE DATABASE authdb;
CREATE DATABASE notifications;

-- Crea rol de aplicación (si lo quieres usar en auth)
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'authuser') THEN
      CREATE ROLE authuser WITH LOGIN PASSWORD 'authpass';
   END IF;
END$$;
GRANT ALL PRIVILEGES ON DATABASE authdb TO authuser;
