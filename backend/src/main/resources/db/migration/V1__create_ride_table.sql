CREATE TABLE ride (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    distance_km NUMERIC(10,2),
    elevation_gain_m NUMERIC(10,2),
    moving_time_seconds BIGINT,
    average_speed_kph NUMERIC(10,2),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);