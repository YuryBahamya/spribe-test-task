# List of Found Issues
This document contains a list of known issues found in the project.

## Create Player API
The main critical issue that create endpoint was implemented through the `GET` method instead of `POST`.
The create endpoint should use `POST`, not `GET`, because GET requests are intended to be safe and idempotent, and should not modify server state.
### Critical Issues
- Returns `null` values for `password`, `screenName`, `gender`, `age` and `role` fields when creating a new player.
- Returns status code 200(with the exiting player data) instead of 400 when create player with the same data twice.
- Returns status code 200(with the exiting player data) instead of 400 when create player existing `login`.
- It's possible to create a player with `age=60` (Note: to be honest, the requirement is not very clear `User should be older than 16 and younger than 60 years old;` Age boundaries should be more strict and clear).
- No validation if the `screenName` field is already in use, allowing duplicate `screenName` registrations.
- No validation for required `password` field and its complexity (e.g., minimum length, special characters) - allowing any string value.
- `admin` role cannot create player with `user` role, returning status code 403 (NOTE: it was working several times during the test task implementation).
- `admin` role was able to create another `admin` role player, which should be restricted. (NOTE: when creation by admin was working)

### Major Issues
- No validation for the `gender` field, allowing any string value.
- No response body for various invalid inputs (e.g., empty age `age=`).
- Returns status code 400 instead of 403 when `user` or `admin` role tries to create a player with `supervisor` role.

## Delete Player API
### Critical Issues
- Player with `admin` role can delete other `admin` players, which should be restricted.
- Player with `user` role can delete other players with `admin` and `user` roles, which should be restricted.
- Player with `user` role can delete himself, which should be restricted.

### Major Issues
- Returns status code 403 instead of 404 when trying to delete a non-existing or already deleted player.

Note: Delete API implemented with sending player data in the request body of `DELETE` method, which is not a recommended practice. The player identifier should be passed as a path parameter or query parameter instead.

## Update Player API
### Critical Issues
- No validation for the `age` field, allowing invalid values (e.g., negative numbers, values out of the requirement range).
- No validation if the `screenName` field is already in use, allowing duplicate `screenName` updates.
- No validation for required `password` field and its complexity (e.g., minimum length, special characters) - allowing any string value.
- `supervisor` role cannot update role of other players (from `user` to `admin` and vice versa)
- `amin` cannot update player with `user` role, 403 status code is returned.
- `user` role can update other players with `admin` and `user` role, which should be restricted.

### Major Issues
- Returns status code 409 instead of 400 when trying to update a player with an existing `login`.
- No validation for the `gender` field, allowing any string value.

## Get Player API
The main critical issue that get endpoint was implemented through the `POST` method instead of `GET`.
Using `POST` for a data retrieval endpoint violates HTTP standards, as `GET` should be used for safe, idempotent, and cacheable operations.
### Critical Issues
- Returns status code 200 instead of 404 when trying to get a non-existing or deleted player.
- Response time is very high, sometimes exceeding 10 seconds

## Get All Players API
### Major Issues
- Does not return `role` field.